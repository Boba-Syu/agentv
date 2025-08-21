package cn.bobasyu.agentv.domain.analysis.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.analysis.vals.AiAnalysisWorkflowId
import cn.bobasyu.agentv.domain.analysis.vals.ChainNodeConfigVal

data class AiAnalysisWorkflowEntity(
    override val id: AiAnalysisWorkflowId,
    val chainNodeConfigs: List<ChainNodeConfigVal>,
) : Entity<AiAnalysisWorkflowId>(id) {
}