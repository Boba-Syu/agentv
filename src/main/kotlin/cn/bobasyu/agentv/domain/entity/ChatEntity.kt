package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.ChatId
import cn.bobasyu.agentv.domain.vals.MessageVal

data class ChatEntity(
    override val id: ChatId,
    var messages: MutableList<MessageVal> = mutableListOf()
) : Entity<ChatId>(id)