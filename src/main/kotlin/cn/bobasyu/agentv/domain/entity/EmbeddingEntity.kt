package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.ModelSourceType
import cn.bobasyu.agentv.domain.vals.RagId
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal
import cn.bobasyu.agentv.infrastructure.repository.command.rag.TextEmbedderHolder

/**
 * RAG配置实体
 */
data class EmbeddingEntity(

    /**
     *  rag id
     */
    override var id: RagId,

    /**
     * 模型
     */
    var modelName: String,

    /**
     * 模型来源
     */
    var sourceType: ModelSourceType,

    /**
     * embedding模型设置
     */
    var embeddingSetting: EmbeddingConfigVal,

    /**
     * 向量维度
     */
    var dimension: Int,
) : Entity<RagId>(id) {
    /**
     * 单文本向量化
     */
    fun embedText(text: String): List<Float> = TextEmbedderHolder.textEmbedder(sourceType).embedText(this, text)

    /**
     * 批量向量化处理
     */
    fun embedBatch(texts: List<String>): List<List<Float>> =
        TextEmbedderHolder.textEmbedder(sourceType).embedBatch(this, texts)
}