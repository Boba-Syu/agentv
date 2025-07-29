package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.McpTransportType

class McpEntity(
    override val id: McpId,
    var name: String,
    var type: McpTransportType,
    var config: McpConfigVal
) : Entity<McpId>(id)