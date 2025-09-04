package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.MetadataFilter
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaEmbeddingModel
import cn.bobasyu.agentv.infrastructure.base.converter.toPgVectorEmbeddingStore
import cn.bobasyu.agentv.infrastructure.base.converter.toTextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.extend.findRelevant
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.ContextRetriever
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment

class LangChainContextRetriever(
    val embeddingEntity: EmbeddingEntity
) : ContextRetriever {
    val embeddingModel = toOllamaEmbeddingModel(embeddingEntity)

    val embeddingStore = toPgVectorEmbeddingStore(embeddingEntity)

    override fun retrieveContext(
        question: String,
        maxResults: Int?,
        filter: List<MetadataFilter>
    ): List<TextSegmentVal> {
        // todo 对用户提问进行预处理
        // 生成问题向量
        val questionEmbedding: Embedding = embeddingModel.embed(question).content()
        // 检索相似段落
        val results: List<TextSegment> = embeddingStore.findRelevant(questionEmbedding, embeddingEntity.embeddingSetting, filter)

        return results.map { toTextSegmentVal(it) }
    }
}