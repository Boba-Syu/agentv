package cn.bobasyu.agentv.infrastructure.repository.command.chat

import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.repository.command.chat.impl.OllamaChatAdapterImpl

interface ChatAdapter {

    /**
     * 聊天，带有智能体配置的记忆，工具，mcp等
     */
    fun chat(agentAggregate: AgentAggregate, message: UserMessageVal): AssistantMessageVal

    /**
     * 聊天, 单纯的模型调用
     */
    fun chat(chatModel: ChatModelEntity, message: UserMessageVal): AssistantMessageVal
}

object ChatAdapterHolder {

    val ollamaChatAdapter: OllamaChatAdapterImpl by lazy { OllamaChatAdapterImpl() }

    fun chatAdapter(modelSourceType: ModelSourceType) :ChatAdapter = when (modelSourceType) {
        ModelSourceType.OPENAI -> TODO()
        ModelSourceType.VOLCENGINE -> TODO()
        ModelSourceType.OLLAMA -> ollamaChatAdapter
    }
}