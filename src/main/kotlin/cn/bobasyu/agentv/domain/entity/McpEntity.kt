package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.McpTransportType

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