package cn.bobasyu.agentv.domain.base.aggregate

import cn.bobasyu.agentv.application.repository.Command.agentRepository
import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.base.entity.AgentEntity
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal

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
    val chatModel: ChatModelEntity get() = agent.chatModel

    /**
     * 获取MCP列表
     */
    val mcpList: List<McpEntity> get() = agent.mcpList

    /**
     * 获取工具列表
     */
    val tools: List<ToolEntity> get() = agent.tools

    /**
     * 聊天
     */
    fun chat(userMessage: UserMessageVal): AssistantMessageVal {
        val assistantMessage = agentRepository.chat(this, userMessage)
        messages.add(assistantMessage)
        return assistantMessage
    }
}