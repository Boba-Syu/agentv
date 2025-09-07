package cn.bobasyu.agentv.infrastructure.base.repository.command

import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.repository.comand.RagRepository
import cn.bobasyu.agentv.domain.base.vals.*
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.*

class RagRepositoryImpl : RagRepository {

    override fun answerQuestion(
        question: String,
        embeddingEntity: EmbeddingEntity,
        chatModelEntity: ChatModelEntity,
        filter: List<MetadataFilter>
    ): AnswerVal {
        val ragChain = RagChainFactory.ragChain(embeddingEntity, chatModelEntity)
        return ragChain.process(UserMessageVal(question))
    }

    override fun storeKnowledge(embeddingEntity: EmbeddingEntity, texts: List<TextSegmentVal>) {
        val textEmbedder = TextEmbedderFactory.textEmbedder(embeddingEntity)
        val vectorList = textEmbedder.embedBatch(texts.map { it.text })

        val vectorStoreAdaptor = VectorStoreAdaptorFactory.textEmbedder(embeddingEntity)
        vectorStoreAdaptor.storeVectors(vectorList, texts)
    }
}