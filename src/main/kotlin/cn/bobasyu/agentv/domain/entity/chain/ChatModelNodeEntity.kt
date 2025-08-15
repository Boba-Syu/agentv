package cn.bobasyu.agentv.domain.entity.chain

import cn.bobasyu.agentv.application.repository.Command.agentRepository
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.ChainNode
import cn.bobasyu.agentv.domain.vals.UserMessageVal


class ChatModelNodeEntity(
    val chatModel: ChatModelEntity
): ChainNode<UserMessageVal, AssistantMessageVal> {
    override fun process(req: UserMessageVal): AssistantMessageVal {
        return agentRepository.chat(chatModel, req);
    }
}