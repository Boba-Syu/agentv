package cn.bobasyu.agentv.domain.analysis.service.command.chain

import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.agentRepository
import cn.bobasyu.agentv.common.utils.parseJson
import cn.bobasyu.agentv.domain.analysis.vals.BaseOutputVal
import cn.bobasyu.agentv.domain.analysis.vals.ChainNodeConfigVal
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.ChainNode
import cn.bobasyu.agentv.domain.base.vals.SystemMessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal

class AiAnalysisChainNode(
    val chatModel: ChatModelEntity,
    val userRole: SystemMessageVal,
) : ChainNode<ChainNodeConfigVal, BaseOutputVal> {

    override fun process(req: ChainNodeConfigVal): BaseOutputVal {
        val message = userRole.message + req.task
        val chat = agentRepository.chat(chatModel, UserMessageVal(message))
        return chat.message.parseJson(req.outputParse.java)
    }
}

class AiAnalysisSaveDataChainNode : ChainNode<BaseOutputVal, Unit> {
    override fun process(req: BaseOutputVal) {
        req.save()
    }
}