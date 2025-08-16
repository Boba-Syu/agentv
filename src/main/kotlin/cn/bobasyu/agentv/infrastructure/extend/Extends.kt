package cn.bobasyu.agentv.infrastructure.extend

import cn.bobasyu.agentv.domain.vals.FilterOperator
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.store.embedding.EmbeddingSearchRequest
import dev.langchain4j.store.embedding.EmbeddingStore

fun EmbeddingStore<TextSegment>.findRelevant(
    embedding: Embedding,
    maxResults: Int?,
    filter: List<MetadataFilter> = listOf()
): List<TextSegment> {
    // 检索相似段落
    val searchRequestBuilder = EmbeddingSearchRequest.builder()
        .queryEmbedding(embedding)
        .maxResults(maxResults)
    if (filter.isNotEmpty()) {
        searchRequestBuilder.filter { metadata ->
            metadata as Map<*, *>
            filter.map { metadataFilter ->
                when (metadataFilter.operator) {
                    FilterOperator.EQUAL -> metadata[metadataFilter.field] == metadataFilter.value
                    FilterOperator.NOT_EQUAL -> metadata[metadataFilter.field] != metadataFilter.value
                    FilterOperator.EXIST -> metadata[metadataFilter.field] != null
                }
            }.any { it }
        }
    }
    return this.search(searchRequestBuilder.build()).matches().map { it.embedded() }
}