package cn.bobasyu.agentv.domain.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.vals.ModelSourceType
import cn.bobasyu.agentv.domain.vals.RagId
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal
import cn.bobasyu.agentv.infrastructure.repository.command.rag.TextEmbedderFactory

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
) : Entity<RagId>(id)