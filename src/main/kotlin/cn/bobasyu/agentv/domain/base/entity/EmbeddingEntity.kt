package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.base.vals.EmbeddingConfigVal
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.RagId

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