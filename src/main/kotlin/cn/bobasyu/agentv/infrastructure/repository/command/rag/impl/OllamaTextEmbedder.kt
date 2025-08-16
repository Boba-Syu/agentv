package cn.bobasyu.agentv.infrastructure.repository.command.rag.impl

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.infrastructure.converter.toOllamaEmbeddingModel
import cn.bobasyu.agentv.infrastructure.repository.command.rag.TextEmbedder
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel

class OllamaTextEmbedder(
    var embeddingEntity: EmbeddingEntity
) : TextEmbedder {
    val embeddingModel: EmbeddingModel = toOllamaEmbeddingModel(embeddingEntity)

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
}
