package cn.bobasyu.agentv.domain.repository.comand

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.AnswerVal
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import cn.bobasyu.agentv.domain.vals.TextSegmentVal

/**
 * RAG核心服务：协调检索和生成过程
 */
interface RagRepository {
    /**
     * 问答服务核心方法
     * @param question 用户问题
     * @param filter 元数据过滤器
     * @return 过滤后的答案
     */
    fun answerQuestion(
        question: String,
        embeddingEntity: EmbeddingEntity,
        chatModelEntity: ChatModelEntity,
        filter: List<MetadataFilter> = listOf()
    ): AnswerVal

    /**
     * 存储知识
     */
    fun storeKnowledge(embeddingEntity: EmbeddingEntity, texts: List<TextSegmentVal>)
}