package cn.bobasyu.agentv.infrastructure.repository.command

import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.config.ApplicationContext
import cn.bobasyu.agentv.domain.aggregate.ChatAggregate
import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.*
import cn.bobasyu.agentv.infrastructure.converter.mcpClients
import cn.bobasyu.agentv.infrastructure.converter.ollamaChatModel
import cn.bobasyu.agentv.infrastructure.record.AgentRecords
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecords
import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.service.AiServices
import org.ktorm.dsl.*


class AgentCommandRepositoryImpl(
    private val applicationContext: ApplicationContext,
    private val agentQueryRepository: AgentQueryRepository
) : AgentCommandRepository {
    val databaseHandler get() = applicationContext.databaseHandler

    override fun chat(
        agentAggregate: ChatAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        return when (agentAggregate.chatModel.sourceType) {
            ChatModelSourceType.OLLAMA -> ollamaChat(agentAggregate, message, mcpList)
            ChatModelSourceType.OPENAI -> TODO()
            ChatModelSourceType.VOLCENGINE -> TODO()
        }
    }

    private fun ollamaChat(
        agentAggregate: ChatAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        val chatModelEntity = agentAggregate.chatModel
        val ollamaChatModel = ollamaChatModel(chatModelEntity)
        val chatMemory = MessageWindowChatMemory.builder()
            .id(chatModelEntity.id)
            .maxMessages(chatModelEntity.config?.maxMessage ?: 10)
            .chatMemoryStore(PersistentChatMemoryStore(this))
            .build()
        val toolProvider = McpToolProvider.builder()
            .mcpClients(mcpClients(mcpList))
            .build()
        val bot = AiServices.builder(Bot::class.java)
            .chatMemory(chatMemory)
            .chatModel(ollamaChatModel)
            .toolProvider(toolProvider)
            .build()

        val response = bot.chat(message.content)
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