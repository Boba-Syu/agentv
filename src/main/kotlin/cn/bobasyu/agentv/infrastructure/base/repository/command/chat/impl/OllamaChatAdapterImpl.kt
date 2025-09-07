package cn.bobasyu.agentv.infrastructure.base.repository.command.chat.impl

import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.AgentAssistant
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.base.converter.toLangChain4jMessage
import cn.bobasyu.agentv.infrastructure.base.converter.toMcpClients
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaChatModel
import cn.bobasyu.agentv.infrastructure.base.converter.toToolExecutor
import cn.bobasyu.agentv.infrastructure.base.converter.toToolSpecification
import cn.bobasyu.agentv.infrastructure.base.repository.RepositoryContext.persistentChatMemoryStore
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.ChatAdapter
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.tool.ToolExecutor
import kotlin.reflect.KClass

class OllamaChatAdapterImpl : ChatAdapter {
    override fun chat(
        agentAggregate: AgentAggregate,
        message: UserMessageVal
    ): AssistantMessageVal {
        val agentAssistant = chatAssistant(AgentAssistant::class, agentAggregate)
        val response = agentAssistant.chat(message.message)
        return AssistantMessageVal(response)
    }

    override fun chat(
        chatModel: ChatModelEntity,
        messages: List<MessageVal>
    ): AssistantMessageVal {
        val ollamaChatModel = toOllamaChatModel(chatModel)
        val chatResponse = ollamaChatModel.chat(messages.map { toLangChain4jMessage(it) })
        return AssistantMessageVal(chatResponse.aiMessage().text())
    }

    override fun <T : Any> chatAssistant(
        clazz: KClass<T>,
        agentAggregate: AgentAggregate
    ): T {
        // 模型
        val chatModelEntity = agentAggregate.chatModel
        val ollamaChatModel = toOllamaChatModel(chatModelEntity)
        // 记忆
        val chatMemory = with(MessageWindowChatMemory.builder()) {
            id(chatModelEntity.id)
            maxMessages(chatModelEntity.config?.maxMessage ?: 10)
            if (agentAggregate.agent.memorySaveFlag) {
                chatMemoryStore(persistentChatMemoryStore)
            }
            build()
        }
        // mcp
        val mcpToolProvider = McpToolProvider.builder()
            .mcpClients(toMcpClients(agentAggregate.mcpList.map { it.config }))
            .build()
        // tools 配置
        val toolSpecificationMap: Map<ToolSpecification, ToolExecutor> = agentAggregate.tools
            .associate { toToolSpecification(it) to toToolExecutor(it.functionCallExecutor) }

        val assistant: T = AiServices.builder(clazz.java)
            .chatModel(ollamaChatModel)
            .chatMemory(chatMemory)
            .toolProvider(mcpToolProvider)
            .tools(toolSpecificationMap)
            .build()
        return assistant
    }

    override fun <T : Any> chatAssistant(
        clazz: KClass<T>,
        chatModelEntity: ChatModelEntity,
        tools: MutableList<ToolEntity>,
        mcpList: MutableList<McpEntity>
    ): T {
        val mcpToolProvider = McpToolProvider.builder()
            .mcpClients(toMcpClients(mcpList.map { it.config }))
            .build()
        val toolSpecificationMap: Map<ToolSpecification, ToolExecutor> = tools
            .associate { toToolSpecification(it) to toToolExecutor(it.functionCallExecutor) }

        val ollamaChatModel = toOllamaChatModel(chatModelEntity)

        val assistant: T = AiServices.builder(clazz.java)
            .chatModel(ollamaChatModel)
            .toolProvider(mcpToolProvider)
            .tools(toolSpecificationMap)
            .build()
        return assistant
    }
}