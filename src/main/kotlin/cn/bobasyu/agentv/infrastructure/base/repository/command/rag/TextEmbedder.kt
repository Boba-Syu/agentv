package cn.bobasyu.agentv.infrastructure.base.repository.command.rag

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.OllamaTextEmbedderAbstract
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.OpenAiTextEmbedderAbstract
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit

/**
 * 文本嵌入适配器：负责文本到向量的转换
 */
interface TextEmbedder {
    /**
     * 单文本向量化
     */
    fun embedText(text: String): List<Float>

    /**
     * 批量向量化处理
     */
    fun embedBatch(texts: List<String>): List<List<Float>>

    fun dimension(): Int
}

object TextEmbedderFactory {

    private val textEmbedderHolder: LoadingCache<EmbeddingEntity, TextEmbedder> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<EmbeddingEntity, TextEmbedder>() {
            override fun load(embeddingEntity: EmbeddingEntity) = when (embeddingEntity.sourceType) {
                ModelSourceType.OLLAMA -> OllamaTextEmbedderAbstract(embeddingEntity)
                else -> OpenAiTextEmbedderAbstract(embeddingEntity)
            }
        })

    fun textEmbedder(embeddingEntity: EmbeddingEntity): TextEmbedder = textEmbedderHolder.get(embeddingEntity)
}