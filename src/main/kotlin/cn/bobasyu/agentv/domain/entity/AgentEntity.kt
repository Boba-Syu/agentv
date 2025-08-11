package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.ToolId

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
    fun mcpList(agentQueryRepository: AgentQueryRepository) = agentQueryRepository.listMcpEntities(mcpIdList)

    /**
     * 获取智能体模型
     */
    fun chatModel(agentQueryRepository: AgentQueryRepository) = agentQueryRepository.findChatModelEntity(chatModelId)

    /**
     * 获取工具列表
     */
    fun tools(agentQueryRepository: AgentQueryRepository) = agentQueryRepository.listToolEntities(toolIdList)
}