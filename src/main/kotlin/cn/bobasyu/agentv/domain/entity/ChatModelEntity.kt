package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.ChatModelConfigVal
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.SystemMessageVal

data class ChatModelEntity(
    override val id: ChatModelId,
    var modelName: String,
    var role: SystemMessageVal?,
    var config: ChatModelConfigVal?,
) : Entity<ChatModelId>(id)