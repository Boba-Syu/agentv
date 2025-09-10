package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaEmbeddingModel
import cn.bobasyu.agentv.infrastructure.base.converter.toOpenAiEmbeddingModel
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.TextEmbedder
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel

class OllamaTextEmbedderAbstract(
    override var embeddingEntity: EmbeddingEntity
) : AbstractLangChainTextEmbedder(embeddingEntity) {
    override fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel =
        toOllamaEmbeddingModel(embeddingEntity)
}

class OpenAiTextEmbedderAbstract(
    override var embeddingEntity: EmbeddingEntity
) : AbstractLangChainTextEmbedder(embeddingEntity) {
    override fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel =
        toOpenAiEmbeddingModel(embeddingEntity)
}


abstract class AbstractLangChainTextEmbedder(
    open var embeddingEntity: EmbeddingEntity
) : TextEmbedder {
    val embeddingModel: EmbeddingModel by lazy { getEmbeddingModel(embeddingEntity) }

    override fun embedText(text: String): List<Float> {
        val embedding = embeddingModel.embed(text).content()
        return embedding.vectorAsList()
    }

    override fun embedBatch(texts: List<String>): List<List<Float>> {
        val textSegments = texts.map { TextSegment.from(it) }
        val embeddings = embeddingModel.embedAll(textSegments).content()
        return embeddings.map { it.vectorAsList() }
    }

    override fun dimension(): Int = embeddingEntity.dimension

    abstract fun getEmbeddingModel(embeddingEntity: EmbeddingEntity): EmbeddingModel
}