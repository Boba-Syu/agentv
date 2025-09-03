package cn.bobasyu.agentv.infrastructure.base.repository.command

import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.repository.comand.RagRepository
import cn.bobasyu.agentv.domain.base.vals.AnswerVal
import cn.bobasyu.agentv.domain.base.vals.MetadataFilter
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.AnswerGeneratorFactory
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.ContextRetrieverFactory
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.TextEmbedderFactory
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.VectorStoreAdaptorFactory

class RagRepositoryImpl : RagRepository {

    override fun answerQuestion(
        question: String,
        embeddingEntity: EmbeddingEntity,
        chatModelEntity: ChatModelEntity,
        filter: List<MetadataFilter>
    ): AnswerVal {
        val startTime = System.currentTimeMillis()
        val contextRetriever = ContextRetrieverFactory.contextRetriever(embeddingEntity)
        val answerGenerator = AnswerGeneratorFactory.answerGenerator(chatModelEntity)
        // 1. 检索上下文
        val context: List<TextSegmentVal> =
            contextRetriever.retrieveContext(question, embeddingEntity.embeddingSetting.maxResults)
        // 2. 生成答案
        val answer = answerGenerator.generateAnswer(question, context)
        // 3. 计算耗时
        val processingTime: Long = System.currentTimeMillis() - startTime

        return AnswerVal(answer, context, processingTime)
    }

    override fun storeKnowledge(embeddingEntity: EmbeddingEntity, texts: List<TextSegmentVal>) {
        val textEmbedder = TextEmbedderFactory.textEmbedder(embeddingEntity)
        val vectorList = textEmbedder.embedBatch(texts.map { it.text })

        val vectorStoreAdaptor = VectorStoreAdaptorFactory.textEmbedder(embeddingEntity)
        vectorStoreAdaptor.storeVectors(vectorList, texts)
    }

}