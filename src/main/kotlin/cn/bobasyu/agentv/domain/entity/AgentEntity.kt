package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.McpId

data class AgentEntity(
    override val id: AgentId,
    var chatModelId: ChatModelId,
    var mcpIdList: MutableList<McpId> = mutableListOf()
) : Aggregate<AgentId>(id)