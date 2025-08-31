package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.application.repository.AgentRepositories.Query.agentQueryRepository
import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.ChatModelId

data class AgentEntity(
    /**
     * 智能体ID
     */
    override val id: AgentId,
    /**
     * 聊天模型ID
     */
    var chatModelId: ChatModelId,

    /**
     * 是否保存聊天记录
     */
    var memorySaveFlag: Boolean = false,
) : Aggregate<AgentId>(id) {

    /**
     * 获取智能体模型
     */
    val chatModel: ChatModelEntity get() = agentQueryRepository.findChatModelEntity(chatModelId)
}