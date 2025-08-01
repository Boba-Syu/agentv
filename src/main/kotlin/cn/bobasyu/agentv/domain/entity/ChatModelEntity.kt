package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.repository.ChatModelRepository
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.ChatModelConfigVal
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.ChatModelSourceType
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.SystemMessageVal

data class ChatModelEntity(
    override val id: ChatModelId,
    var modelName: String,
    var role: SystemMessageVal?,
    var config: ChatModelConfigVal?,
    var sourceType: ChatModelSourceType
) : Entity<ChatModelId>(id) {

    fun chat(messages: List<MessageVal>, mcpList: List<McpConfigVal> = listOf()): AssistantMessageVal {
        return ChatModelRepository.INSTANCE.chat(messages, mcpList)
    }
}