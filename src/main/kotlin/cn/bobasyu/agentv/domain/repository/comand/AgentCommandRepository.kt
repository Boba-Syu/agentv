package cn.bobasyu.agentv.domain.repository.comand

import cn.bobasyu.agentv.common.repository.BaseCommandRepository
import cn.bobasyu.agentv.domain.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.*

interface AgentCommandRepository : BaseCommandRepository<AgentQueryRepository> {

    /**
     * 聊天
     */
    fun chat(
        agentAggregate: AgentAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal

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