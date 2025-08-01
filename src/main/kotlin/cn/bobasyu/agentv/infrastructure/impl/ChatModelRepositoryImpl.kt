package cn.bobasyu.agentv.infrastructure.impl

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.repository.ChatModelRepository
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.ChatModelSourceType
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.infrastructure.converter.langChain4jMessages
import cn.bobasyu.agentv.infrastructure.converter.mcpClients
import cn.bobasyu.agentv.infrastructure.converter.ollamaChatModel
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.mcp.McpToolProvider
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.service.AiServices

interface Bot {
    fun chat(message: String): String
}

class ChatModelRepositoryImpl : ChatModelRepository {
    override fun chat(
        chatModelEntity: ChatModelEntity,
        messages: List<MessageVal>,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        return when (chatModelEntity.sourceType) {
            ChatModelSourceType.OLLAMA -> ollamaChat(chatModelEntity, messages, mcpList)
            ChatModelSourceType.OPENAI -> TODO()
            ChatModelSourceType.VOLCENGINE -> TODO()
        }
    }

    fun ollamaChat(
        chatModelEntity: ChatModelEntity,
        messages: List<MessageVal>,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal {
        val ollamaChatModel = ollamaChatModel(chatModelEntity)
        val chtRequest = ChatRequest.builder()
            .messages(langChain4jMessages(messages))
            .toolSpecifications(listOf<ToolSpecification>()) // todo
            .build()
        val toolProvider = McpToolProvider.builder()
            .mcpClients(mcpClients(mcpList))
            .build()
        val bot = AiServices.builder(Bot::class.java)
            .chatModel(ollamaChatModel)
            .toolProvider(toolProvider)
            .build()

        val response = ollamaChatModel.chat(chtRequest)

        return AssistantMessageVal(response.aiMessage().text())
    }
}