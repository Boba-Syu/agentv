package cn.bobasyu.agentv.infrastructure.repository.command.rag

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import cn.bobasyu.agentv.domain.vals.ModelSourceType
import cn.bobasyu.agentv.domain.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.repository.command.rag.impl.LangChainContextRetriever


/**
 * 检索器接口：封装上下文检索逻辑
 */
interface ContextRetriever {
    /**
     * 检索与问题相关的上下文片段
     * @param question 用户问题
     * @param maxResults 最大返回片段数
     * @return 相关文本片段
     */
    fun retrieveContext(
        question: String,
        maxResults: Int? = null,
        filter: List<MetadataFilter> = listOf()
    ): List<TextSegmentVal>
}

object ContextRetrieverFactory {

    fun contextRetriever(embeddingEntity: EmbeddingEntity): ContextRetriever = when (embeddingEntity.sourceType) {
        ModelSourceType.OLLAMA -> LangChainContextRetriever(embeddingEntity)
        else -> TODO()
    }
}
