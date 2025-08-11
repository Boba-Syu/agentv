package cn.bobasyu.agentv.domain.aggregate

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.entity.ToolEntity
import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.UserMessageVal

/**
 * 智能体聚合实体
 */
data class AgentAggregate(
    /**
     * 智能体entity
     */
    val agent: AgentEntity,
    /**
     * 消息列表
     */
    var messages: MutableList<MessageVal> = mutableListOf(),
) : Aggregate<AgentId>(agent.id) {

    /**
     * 获取智能体模型
     */
    fun chatModel(agentQueryRepository: AgentQueryRepository): ChatModelEntity = agent.chatModel(agentQueryRepository)

    /**
     * 获取MCP列表
     */
    fun mcpList(agentQueryRepository: AgentQueryRepository): List<McpEntity> = agent.mcpList(agentQueryRepository)

    /**
     * 获取工具列表
     */
    fun tools(agentQueryRepository: AgentQueryRepository): List<ToolEntity> = agent.tools(agentQueryRepository)

    /**
     * 聊天
     */
    fun chat(userMessage: UserMessageVal, agentCommandRepository: AgentCommandRepository): AssistantMessageVal {
        val mcpConfigVals: List<McpConfigVal> = agentCommandRepository.query().listMcpEntities(agent.mcpIdList)
            .map { it.config }
            .toList()
        val assistantMessage = agentCommandRepository.chat(this, userMessage, mcpConfigVals)
        messages.add(assistantMessage)
        return assistantMessage
    }
}