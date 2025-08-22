package cn.bobasyu.agentv.domain.analysis.aggregate

import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.analysis.entity.AiAnalysisWorkflowEntity
import cn.bobasyu.agentv.domain.analysis.service.command.chain.AiAnalysisChainNode
import cn.bobasyu.agentv.domain.analysis.service.command.chain.AiAnalysisSaveDataChainNode
import cn.bobasyu.agentv.domain.analysis.vals.AiAnalysisWorkflowId
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.Chain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

data class AiAnalysisWorkFlowAggregate(
    val aiAnalysisWorkflowEntity: AiAnalysisWorkflowEntity,
    val chatModelEntity: ChatModelEntity,
) : Aggregate<AiAnalysisWorkflowId>(aiAnalysisWorkflowEntity.id) {

    fun process(): Job {
        val chainList = aiAnalysisWorkflowEntity.chainNodeConfigs
            .map { chainNodeConfigVal ->
                val analysisChainNode = AiAnalysisChainNode(
                    chatModel = chatModelEntity,
                    userRole = chatModelEntity.role!!,
                )
                val saveChainNode = AiAnalysisSaveDataChainNode()
                Chain {
                    (analysisChainNode + saveChainNode).process(chainNodeConfigVal)
                }
            }
        return CoroutineScope(Dispatchers.Default).launch {
            chainList.map { launch { it.execute() } }
                .joinAll()
        }
    }
}