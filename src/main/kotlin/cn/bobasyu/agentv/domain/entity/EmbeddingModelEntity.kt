package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.domain.vals.EmbeddingModelId
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal

data class EmbeddingModelEntity(
    var embeddingModelId: EmbeddingModelId,
    var modelName: String,
    var embeddingSetting: EmbeddingConfigVal
) {
}