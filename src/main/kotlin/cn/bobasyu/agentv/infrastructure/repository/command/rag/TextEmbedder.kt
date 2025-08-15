package cn.bobasyu.agentv.infrastructure.repository.command.rag

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.ModelSourceType
import cn.bobasyu.agentv.infrastructure.repository.command.rag.impl.OllamaTextEmbedder

/**
 * 文本嵌入适配器：负责文本到向量的转换
 */
interface TextEmbedder {
    /**
     * 单文本向量化
     */
    fun embedText(embeddingEntity: EmbeddingEntity, text: String): List<Float>

    /**
     * 批量向量化处理
     */
    fun embedBatch(embeddingEntity: EmbeddingEntity, texts: List<String>): List<List<Float>>
}

object TextEmbedderHolder {

    val ollamaTextEmbedder: OllamaTextEmbedder by lazy { OllamaTextEmbedder() }

    fun textEmbedder(sourceType: ModelSourceType): TextEmbedder = when (sourceType) {
        ModelSourceType.OPENAI -> TODO()
        ModelSourceType.VOLCENGINE -> TODO()
        ModelSourceType.OLLAMA -> ollamaTextEmbedder
    }
}