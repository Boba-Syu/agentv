package cn.bobasyu.agentv.domain.repository

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageVal

interface ChatModelRepository {

    companion object {
        val INSTANCE: ChatModelRepository by lazy { chatModelRepository() }
    }

    fun chat(
        chatModelEntity: ChatModelEntity,
        messages: List<MessageVal>,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal
}

fun chatModelRepository(): ChatModelRepository {
    TODO()
}