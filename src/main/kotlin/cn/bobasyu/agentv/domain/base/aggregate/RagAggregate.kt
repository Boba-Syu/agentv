package cn.bobasyu.agentv.domain.base.aggregate

import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.ragRepository
import cn.bobasyu.agentv.common.vals.Aggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.AnswerVal
import cn.bobasyu.agentv.domain.base.vals.MetadataFilter
import cn.bobasyu.agentv.domain.base.vals.RagId
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal

/**
 * rag 模型聚合
 */
data class RagAggregate(
    /**
     * 嵌入模型
     */
    val embeddingEntity: EmbeddingEntity,
    /**
     * 聊天模型
     */
    val chatModelEntity: ChatModelEntity
) : Aggregate<RagId>(embeddingEntity.id) {

    /**
     *  rag 模型回答问题
     */
    fun answerQuestion(question: String, filter: List<MetadataFilter> = listOf()): AnswerVal =
        ragRepository.answerQuestion(question, embeddingEntity, chatModelEntity, filter)

    /**
     * 存储知识
     */
    fun storeKnowledge(texts: List<TextSegmentVal>) {
        ragRepository.storeKnowledge(embeddingEntity, texts)
    }
}