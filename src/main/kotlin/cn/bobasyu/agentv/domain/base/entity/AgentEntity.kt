package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.application.repository.Query.agentQueryRepository
import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.McpId
import cn.bobasyu.agentv.domain.base.vals.ToolId

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
     * 智能体调用MCP ID列表
     */
    var mcpIdList: MutableList<McpId> = mutableListOf(),
    /**
     * 工具ID列表
     */
    var toolIdList: MutableList<ToolId> = mutableListOf(),
) : Aggregate<AgentId>(id) {

    /**
     * 获取MCP列表
     */
    val mcpList: List<McpEntity> get() = agentQueryRepository.listMcpEntities(mcpIdList)

    /**
     * 获取智能体模型
     */
    val chatModel: ChatModelEntity get() = agentQueryRepository.findChatModelEntity(chatModelId)

    /**
     * 获取工具列表
     */
    val tools: List<ToolEntity> get() = agentQueryRepository.listToolEntities(toolIdList)
}