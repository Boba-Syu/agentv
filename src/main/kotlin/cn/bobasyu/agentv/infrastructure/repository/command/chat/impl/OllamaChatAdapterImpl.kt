package cn.bobasyu.agentv.infrastructure.repository.command.chat.impl

import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.AgentAssistant
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.converter.toMcpClients
import cn.bobasyu.agentv.infrastructure.converter.toOllamaChatModel
import cn.bobasyu.agentv.infrastructure.converter.toToolExecutor
import cn.bobasyu.agentv.infrastructure.converter.toToolSpecification
import cn.bobasyu.agentv.infrastructure.repository.RepositoryContext.persistentChatMemoryStore
import cn.bobasyu.agentv.infrastructure.repository.command.chat.ChatAdapter
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.tool.ToolExecutor

class OllamaChatAdapterImpl: ChatAdapter {
    override fun chat(
        agentAggregate: AgentAggregate,
        message: UserMessageVal
    ): AssistantMessageVal {
        // 模型
        val chatModelEntity = agentAggregate.chatModel
        val ollamaChatModel = toOllamaChatModel(chatModelEntity)
        // 记忆
        val chatMemory = MessageWindowChatMemory.builder()
            .id(chatModelEntity.id)
            .maxMessages(chatModelEntity.config?.maxMessage ?: 10)
            .chatMemoryStore(persistentChatMemoryStore)
            .build()
        // mcp
        val mcpToolProvider = McpToolProvider.builder()
            .mcpClients(toMcpClients(agentAggregate.mcpList.map { it.config }))
            .build()
        // tools 配置
        val toolSpecificationMap: Map<ToolSpecification, ToolExecutor> = agentAggregate.tools
            .associate { toToolSpecification(it) to toToolExecutor(it.functionCallExecutor) }

        val agentAssistant = AiServices.builder(AgentAssistant::class.java)
            .chatMemory(chatMemory)
            .chatModel(ollamaChatModel)
            .toolProvider(mcpToolProvider)
            .tools(toolSpecificationMap)
            .build()
        val response = agentAssistant.chat(message.content)
        return AssistantMessageVal(response)
    }

    override fun chat(
        chatModel: ChatModelEntity,
        message: UserMessageVal
    ): AssistantMessageVal {
        val ollamaChatModel = toOllamaChatModel(chatModel)
        val agentAssistant = AiServices.builder(AgentAssistant::class.java)
            .chatModel(ollamaChatModel)
            .build()
        val response = agentAssistant.chat(message.content)
        return AssistantMessageVal(response)
    }
}