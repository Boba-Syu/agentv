package cn.bobasyu.agentv.infrastructure.repository.command.rag.impl

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import cn.bobasyu.agentv.domain.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.converter.toPgVectorEmbeddingStore
import cn.bobasyu.agentv.infrastructure.converter.toTextSegment
import cn.bobasyu.agentv.infrastructure.converter.toTextSegmentVal
import cn.bobasyu.agentv.infrastructure.extend.findRelevant
import cn.bobasyu.agentv.infrastructure.repository.command.rag.VectorStoreAdaptor
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment


class OllamaVectorStoreAdapter(
    var embeddingEntity: EmbeddingEntity,
) : VectorStoreAdaptor {

    private val embeddingStore = toPgVectorEmbeddingStore(embeddingEntity)

    override fun initStorage(dimensions: Int) {
        TODO("Not yet implemented")
    }

    override fun storeVectors(vectors: List<List<Float>>, segments: List<TextSegmentVal>) {
        // 转换为 LangChain4j 的嵌入对象
        val embeddings = vectors.map { Embedding.from(it) }
        // 转换为 LangChain4j 的文本分段
        val langChainSegments: List<TextSegment> = segments.map { toTextSegment(it) }
        // 批量存储
        embeddingStore.addAll(embeddings, langChainSegments)
    }

    override fun similaritySearch(
        queryVector: List<Float>,
        maxResults: Int,
        filter: List<MetadataFilter>
    ): List<TextSegmentVal> {
        val queryEmbedding = Embedding.from(queryVector)
        val results: List<TextSegment> = embeddingStore.findRelevant(queryEmbedding, maxResults, filter)
        return results.map { toTextSegmentVal(it) }
    }
}