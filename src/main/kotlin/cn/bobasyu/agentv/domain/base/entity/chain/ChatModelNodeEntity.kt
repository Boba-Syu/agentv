package cn.bobasyu.agentv.domain.base.entity.chain

import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.agentRepository
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.ChainNode
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal


class ChatModelNodeEntity(
    val chatModel: ChatModelEntity
) : ChainNode<UserMessageVal, AssistantMessageVal> {
    override fun process(req: UserMessageVal): AssistantMessageVal {
        return agentRepository.chat(chatModel, req)
    }
}