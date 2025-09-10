package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.MetadataFilter
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaEmbeddingModel
import cn.bobasyu.agentv.infrastructure.base.converter.toOpenAiEmbeddingModel
import cn.bobasyu.agentv.infrastructure.base.converter.toPgVectorEmbeddingStore
import cn.bobasyu.agentv.infrastructure.base.converter.toTextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.extend.findRelevant
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.ContextRetriever
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel


class OllamaContextRetriever(
    override val embeddingEntity: EmbeddingEntity
) : LangChainContextRetriever(embeddingEntity) {
    override fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel =
        toOllamaEmbeddingModel(embeddingEntity)
}

class OpenAiContextRetriever(
    override val embeddingEntity: EmbeddingEntity
) : LangChainContextRetriever(embeddingEntity) {
    override fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel =
        toOpenAiEmbeddingModel(embeddingEntity)
}

abstract class LangChainContextRetriever(
    open val embeddingEntity: EmbeddingEntity
) : ContextRetriever {
    val embeddingModel by lazy { getEmbeddingModel(embeddingEntity) }

    val embeddingStore by lazy { toPgVectorEmbeddingStore(embeddingEntity) }

    override fun retrieveContext(
        question: String,
        maxResults: Int?,
        filter: List<MetadataFilter>
    ): List<TextSegmentVal> {
        // 生成问题向量
        val questionEmbedding: Embedding = embeddingModel.embed(question).content()
        // 检索相似段落
        val results: List<TextSegment> =
            embeddingStore.findRelevant(questionEmbedding, embeddingEntity.embeddingSetting, filter)

        return results.map { toTextSegmentVal(it) }
    }

    abstract fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel
}
