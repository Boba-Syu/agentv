package cn.bobasyu.agentv.infrastructure.repository.command.rag.impl

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.FilterOperator
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import cn.bobasyu.agentv.domain.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.converter.embeddingStore
import cn.bobasyu.agentv.infrastructure.repository.command.rag.VectorStoreAdaptor
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.store.embedding.EmbeddingSearchRequest
import java.util.stream.Collectors


class OllamaVectorStoreAdapter : VectorStoreAdaptor {

    override fun initStorage(
        embeddingEntity: EmbeddingEntity,
        dimensions: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun storeVectors(
        embeddingEntity: EmbeddingEntity,
        vectors: List<List<Float>>,
        segments: List<TextSegmentVal>
    ) {
        val embeddingStore = embeddingStore(embeddingEntity)

        // 转换为 LangChain4j 的嵌入对象
        val embeddings = vectors.stream()
            .map(Embedding::from)
            .collect(Collectors.toList())

        // 转换为 LangChain4j 的文本分段
        val langChainSegments: List<TextSegment> = segments
            .map { toLangChainSegment(it) }

        // 批量存储
        embeddingStore.addAll(embeddings, langChainSegments)
    }

    override fun similaritySearch(
        embeddingEntity: EmbeddingEntity,
        queryVector: MutableList<Float>,
        maxResults: Int
    ): List<TextSegmentVal> {
        val embeddingStore = embeddingStore(embeddingEntity)
        val queryEmbedding = Embedding.from(queryVector)
        val searchRequest: EmbeddingSearchRequest = EmbeddingSearchRequest.builder()
            .queryEmbedding(queryEmbedding)
            .maxResults(maxResults)
            .build()
        val results: List<TextSegment> = embeddingStore.search(searchRequest).matches().map { it.embedded() }

        return results.map { toDomainSegment(it) }
    }

    override fun similaritySearchWithFilter(
        embeddingEntity: EmbeddingEntity,
        queryVector: List<Float>,
        filter: List<MetadataFilter>,
        maxResults: Int
    ): List<TextSegmentVal> {
        val embeddingStore = embeddingStore(embeddingEntity)
        val queryEmbedding = Embedding.from(queryVector)
        val searchRequest: EmbeddingSearchRequest = EmbeddingSearchRequest.builder()
            .queryEmbedding(queryEmbedding)
            .maxResults(maxResults)
            .filter { metadata ->
                metadata as Map<*, *>
                filter.map { metadataFilter ->
                    when (metadataFilter.operator) {
                        FilterOperator.EQUAL -> metadata[metadataFilter.field] == metadataFilter.value
                        FilterOperator.NOT_EQUAL -> metadata[metadataFilter.field] != metadataFilter.value
                        FilterOperator.EXIST -> metadata[metadataFilter.field] != null
                    }
                }.any{ it }
            }.build()
        val results: List<TextSegment> = embeddingStore.search(searchRequest).matches().map { it.embedded() }
        return results.map { toDomainSegment(it) }
    }

    // 类型转换辅助方法
    private fun toLangChainSegment(domainSegment: TextSegmentVal): TextSegment {
        val metadata = dev.langchain4j.data.document.Metadata(domainSegment.metadata)
        return TextSegment.from(domainSegment.text, metadata)
    }

    private fun toDomainSegment(langChainSegment: TextSegment): TextSegmentVal =
        TextSegmentVal(
            text = langChainSegment.text(),
            metadata = langChainSegment.metadata().toMap() ?: mutableMapOf(),
        )

}