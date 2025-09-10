package cn.bobasyu.agentv.infrastructure.base.repository.command.chat

import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.impl.OllamaChatAdapterImpl
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.impl.OpenAIChatAdapterImpl
import kotlin.reflect.KClass

interface ChatAdapter {

    /**
     * 聊天，带有智能体配置的记忆，工具，mcp等
     */
    fun chat(agentAggregate: AgentAggregate, message: UserMessageVal): AssistantMessageVal

    /**
     * 聊天, 单纯的模型调用
     */
    fun chat(chatModel: ChatModelEntity, messages: List<MessageVal>): AssistantMessageVal

    /**
     * 聊天，返回指定类型的智能体
     */
    fun <T : Any> chatAssistant(clazz: KClass<T>, agentAggregate: AgentAggregate): T

    /**
     * 聊天，返回指定类型的智能体
     */
    fun <T : Any> chatAssistant(
        clazz: KClass<T>,
        chatModelEntity: ChatModelEntity,
        tools: MutableList<ToolEntity> = mutableListOf(),
        mcpList: MutableList<McpEntity> = mutableListOf()
    ): T
}

object ChatAdapterHolder {

    val ollamaChatAdapter: OllamaChatAdapterImpl by lazy { OllamaChatAdapterImpl() }

    val openAIChatAdapter: OpenAIChatAdapterImpl by lazy { OpenAIChatAdapterImpl() }

    fun chatAdapter(modelSourceType: ModelSourceType): ChatAdapter = when (modelSourceType) {
        ModelSourceType.OPENAI,ModelSourceType.VOLCENGINE -> openAIChatAdapter
        ModelSourceType.OLLAMA -> ollamaChatAdapter
    }
}