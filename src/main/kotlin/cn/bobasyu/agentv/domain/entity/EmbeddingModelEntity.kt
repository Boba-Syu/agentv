package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.EmbeddingModelId
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal

data class EmbeddingModelEntity(
    override var id: EmbeddingModelId,
    var modelName: String,
    var embeddingSetting: EmbeddingConfigVal
) : Entity<EmbeddingModelId>(id) {
}