package cn.bobasyu.agentv.domain.aggregate

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.vals.*

data class ChatAggregate(
    val agent: AgentEntity,
    var chatModel: ChatModelEntity,
    var messages: MutableList<MessageVal> = mutableListOf(),
) : Aggregate<AgentId>(agent.id) {

    fun chat(userMessage: UserMessageVal, agentCommandRepository: AgentCommandRepository): AssistantMessageVal {
        val mcpConfigVals: List<McpConfigVal> = agentCommandRepository.query().listMcpEntities(agent.mcpIdList)
            .map { it.config }
            .toList()
        val assistantMessage = agentCommandRepository.chat(this, userMessage, mcpConfigVals)
        messages.add(assistantMessage)
        return assistantMessage
    }
}