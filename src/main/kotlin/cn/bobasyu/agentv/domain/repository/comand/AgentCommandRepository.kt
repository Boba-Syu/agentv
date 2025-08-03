package cn.bobasyu.agentv.domain.repository.comand

import cn.bobasyu.agentv.common.repository.BaseCommandRepository
import cn.bobasyu.agentv.domain.aggregate.ChatAggregate
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.*

interface AgentCommandRepository : BaseCommandRepository<AgentQueryRepository> {

    fun chat(
        agentAggregate: ChatAggregate,
        message: UserMessageVal,
        mcpList: List<McpConfigVal>
    ): AssistantMessageVal

    fun updateMessages(
        agentId: AgentId,
        messages: List<MessageVal>
    )

    fun deleteMessages(agentId: AgentId)

    fun createNewAgent(chatModelId: ChatModelId, mcpIds: List<McpId>): AgentId
}