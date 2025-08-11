package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.RagId
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal

/**
 * RAG配置实体
 */
data class RagEntity(
    /**
     *  rag id
     */
    override var id: RagId,
    /**
     * rag模型
     */
    var model: String,
    /**
     * embedding模型设置
     */
    var embeddingSetting: EmbeddingConfigVal
) : Entity<RagId>(id) {
}