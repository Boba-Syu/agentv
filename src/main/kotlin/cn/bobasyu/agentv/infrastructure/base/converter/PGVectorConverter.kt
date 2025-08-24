package cn.bobasyu.agentv.infrastructure.base.converter

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore

fun toPgVectorEmbeddingStore(embeddingEntity: EmbeddingEntity): PgVectorEmbeddingStore {
    // todo
    return PgVectorEmbeddingStore.builder()
        .database("") // todo
        .table("") // todo
        .user("")
        .password("")
        .dimension(embeddingEntity.dimension)
        .build()
}