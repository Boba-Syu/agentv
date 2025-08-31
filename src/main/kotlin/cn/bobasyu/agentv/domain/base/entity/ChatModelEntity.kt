package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.agentRepository
import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.ChatModelConfigVal
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.SystemMessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal

/**
 * 聊天模型实体
 */
data class ChatModelEntity(
    /**
     * 模型id
     */
    override val id: ChatModelId,
    /**
     * 模型名称
     */
    var modelName: String,
    /**
     * 系统角色
     */
    var role: SystemMessageVal?,
    /**
     * 模型配置
     */
    var config: ChatModelConfigVal?,
    /**
     * 模型来源
     */
    var sourceType: ModelSourceType
) : Entity<ChatModelId>(id) {

    /**
     * 聊天
     */
    fun chat(message: UserMessageVal): AssistantMessageVal = agentRepository.chat(this, message)
}