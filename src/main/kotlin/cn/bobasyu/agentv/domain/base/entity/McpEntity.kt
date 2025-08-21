package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.base.vals.McpConfigVal
import cn.bobasyu.agentv.domain.base.vals.McpId
import cn.bobasyu.agentv.domain.base.vals.McpTransportType

/**
 * MCP配置实体
 */
class McpEntity(
    /**
     * MCP ID
     */
    override val id: McpId,
    /**
     * MCP名称
     */
    var name: String,
    /**
     * MCP传输方式
     */
    var type: McpTransportType,
    /**
     * MCP配置
     */
    var config: McpConfigVal
) : Entity<McpId>(id)