package cn.bobasyu.agentv.infrastructure.base.converter

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.config.EmbeddingConfig
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore

fun toPgVectorEmbeddingStore(embeddingEntity: EmbeddingEntity): PgVectorEmbeddingStore {
    val embeddingConfig = ApplicationContext.instance.config[EmbeddingConfig::class]
    val tableName = getTableName(embeddingEntity)  // 去除首尾下划线

    return PgVectorEmbeddingStore.builder()
        .host(embeddingConfig.host)
        .port(embeddingConfig.port)
        .database(embeddingConfig.database)
        .user(embeddingConfig.user)
        .password(embeddingConfig.password)
        .dimension(embeddingEntity.dimension)
        .table(tableName)
        .useIndex(true)
        .indexListSize(100)
        .build()
}

private fun getTableName(embeddingEntity: EmbeddingEntity): String =
    "${embeddingEntity.modelName}_embedding_record"
        .replace(Regex("_+"), "_")  // 替换多个下划线为单个
        .replace("-", "_")
        .replace(":", "_")
        .trim('_')