package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.ChatModelConfigVal
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.ChatModelSourceType
import cn.bobasyu.agentv.domain.vals.SystemMessageVal

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
    var sourceType: ChatModelSourceType
) : Entity<ChatModelId>(id)