package cn.bobasyu.agentv.domain.base.repository.comand

import cn.bobasyu.agentv.common.repository.BaseCommandRepository
import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.McpId
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal

interface AgentRepository : BaseCommandRepository<AgentQueryRepository> {

    /**
     * 聊天，带有智能体配置的记忆，工具，mcp等
     */
    fun chat(agentAggregate: AgentAggregate, message: UserMessageVal): AssistantMessageVal

    /**
     * 聊天, 单纯的模型调用
     */
    fun chat(chatModel: ChatModelEntity, message: UserMessageVal): AssistantMessageVal

    /**
     * 保存模型
     */
    fun saveModel(chatModel: ChatModelEntity)

    /**
     * 保存消息
     */
    fun saveMessages(
        agentId: AgentId,
        messages: List<MessageVal>
    )

    /**
     * 删除消息
     */
    fun deleteMessages(agentId: AgentId)

    /**
     * 创建新代理
     */
    fun createNewAgent(chatModelId: ChatModelId, mcpIds: List<McpId>): AgentId
}