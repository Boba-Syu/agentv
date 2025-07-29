package cn.bobasyu.agentv.domain.aggregate

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.entity.ChatEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.ChatId
import cn.bobasyu.agentv.domain.vals.UserMessageVal

data class AgentAggregate(
    val chatRecord: ChatEntity,
    var model: ChatModelEntity,
    var mcp: MutableList<McpEntity> = mutableListOf()
) : Aggregate<ChatId>(chatRecord.id) {

    fun chat(userMessage: UserMessageVal) : AssistantMessageVal {
        TODO()
    }
}