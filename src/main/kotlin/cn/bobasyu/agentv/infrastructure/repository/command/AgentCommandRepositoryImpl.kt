package cn.bobasyu.agentv.infrastructure.repository.command

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.domain.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.AgentAssistant
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.ChatModelSourceType
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.converter.mcpClients
import cn.bobasyu.agentv.infrastructure.converter.ollamaChatModel
import cn.bobasyu.agentv.infrastructure.converter.toolExecutor
import cn.bobasyu.agentv.infrastructure.converter.toolSpecification
import cn.bobasyu.agentv.infrastructure.record.AgentRecords
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecords
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.tool.ToolExecutor
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where


class AgentCommandRepositoryImpl(
    private val agentQueryRepository: AgentQueryRepository,
    private val databaseHandler: DatabaseHandler,
) : AgentCommandRepository {

    override fun chat(
        agentAggregate: AgentAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        return when (agentAggregate.chatModel(query()).sourceType) {
            ChatModelSourceType.OLLAMA -> ollamaChat(agentAggregate, message, mcpList)
            ChatModelSourceType.OPENAI -> TODO()
            ChatModelSourceType.VOLCENGINE -> TODO()
        }
    }

    private fun ollamaChat(
        agentAggregate: AgentAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        // 模型
        val chatModelEntity = agentAggregate.chatModel(query())
        val ollamaChatModel = ollamaChatModel(chatModelEntity)
        // 记忆
        val chatMemory = MessageWindowChatMemory.builder()
            .id(chatModelEntity.id)
            .maxMessages(chatModelEntity.config?.maxMessage ?: 10)
            .chatMemoryStore(PersistentChatMemoryStore(this))
            .build()
        // mcp
        val mcpToolProvider = McpToolProvider.builder()
            .mcpClients(mcpClients(mcpList))
            .build()
        // tools 配置
        val toolSpecificationMap: Map<ToolSpecification, ToolExecutor> = agentAggregate.tools(query())
            .associate { toolSpecification(it) to toolExecutor(it.functionCallExecutor) }

        val agentAssistant = AiServices.builder(AgentAssistant::class.java)
            .chatMemory(chatMemory)
            .chatModel(ollamaChatModel)
            .toolProvider(mcpToolProvider)
            .tools(toolSpecificationMap)
            .build()
        val response = agentAssistant.chat(message.content)
        return AssistantMessageVal(response)
    }

    override fun saveMessages(
        agentId: AgentId,
        messages: List<MessageVal>
    ) {
        databaseHandler.batchInsert(ChatMessageRecords) {
            messages.map { chatMessage ->
                item {
                    set(it.id, generateId())
                    set(ChatMessageRecords.agentId, agentId.value)
                    set(ChatMessageRecords.message, chatMessage.content)
                    set(ChatMessageRecords.role, chatMessage.role.name.lowercase())
                }
            }
        }
    }

    override fun deleteMessages(agentId: AgentId) {
        val messageRecords = databaseHandler.from(ChatMessageRecords)
            .select()
            .apply {
                where { ChatMessageRecords.agentId eq agentId.value }
                where { ChatMessageRecords.deleteFlag eq false }
            }.orderBy(ChatMessageRecords.createAt.asc())
            .map { row -> ChatMessageRecords.createEntity(row) }

        databaseHandler.batchUpdate(ChatMessageRecords) {
            messageRecords.map { messageRecord ->
                item {
                    set(it.id, messageRecord.id)
                    set(ChatMessageRecords.deleteFlag, false)
                }
            }
        }
    }

    override fun createNewAgent(chatModelId: ChatModelId, mcpIds: List<McpId>): AgentId {
        val agentId = generateId()
        val mcpIdList = mcpIds.map { it.value }
        databaseHandler.insert(AgentRecords) {
            set(it.id, agentId)
            set(it.chatModelId, chatModelId.value)
            set(it.mcp, mcpIdList)
        }
        return AgentId(agentId)
    }

    override fun query() = agentQueryRepository
}