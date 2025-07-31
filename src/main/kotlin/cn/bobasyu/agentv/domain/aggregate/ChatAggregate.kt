package cn.bobasyu.agentv.domain.aggregate

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.repository.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.UserMessageVal

data class ChatAggregate(
    val agent: AgentEntity,
    var chatModel: ChatModelEntity,
    var messages: MutableList<MessageVal> = mutableListOf(),
    ) : Aggregate<AgentId>(agent.id) {

    fun chat(userMessage: UserMessageVal): AssistantMessageVal {
        messages.add(userMessage)
        val mcpConfigVals = AgentQueryRepository.INSTANCE.listMcpEntities(agent.mcpIdList)
            .map { it.config }
            .toList()
        val assistantMessage = chatModel.chat(messages, mcpConfigVals)
        messages.add(assistantMessage)
        return assistantMessage
    }
}