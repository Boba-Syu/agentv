package cn.bobasyu.agentv.infrastructure.converter

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore

fun embeddingStore(embeddingEntity: EmbeddingEntity): PgVectorEmbeddingStore {
    // todo
    return PgVectorEmbeddingStore.builder().build()
}