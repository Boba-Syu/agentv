package cn.bobasyu.agentv.infrastructure.repository.command.rag.impl

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.infrastructure.converter.ollamaEmbeddingModel
import cn.bobasyu.agentv.infrastructure.repository.command.rag.TextEmbedder
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel

class OllamaTextEmbedder : TextEmbedder {
    override fun embedText(embeddingEntity: EmbeddingEntity, text: String): List<Float> {
        val embeddingModel: EmbeddingModel = ollamaEmbeddingModel(embeddingEntity)
        val embedding = embeddingModel.embed(text).content()
        return embedding.vectorAsList()
    }

    override fun embedBatch(embeddingEntity: EmbeddingEntity, texts: List<String>): List<List<Float>> {
        val embeddingModel: EmbeddingModel = ollamaEmbeddingModel(embeddingEntity)
        val textSegments = texts.map { TextSegment.from(it) }
        val embeddings = embeddingModel.embedAll(textSegments).content()
        return embeddings.map { it.vectorAsList() }
    }
}