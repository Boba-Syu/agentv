package cn.bobasyu.agentv.infrastructure.base.repository.command.rag

import cn.bobasyu.agentv.application.service.AgentServices.Factory.agentArmoryFactory
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.*
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.ChatAdapterHolder
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.VolcengineScoringModel
import dev.langchain4j.service.SystemMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.java

object RagChainFactory {
    fun ragChain(
        embeddingEntity: EmbeddingEntity,
        chatModelEntity: ChatModelEntity,
    ): ChainNode<UserMessageVal, AnswerVal> {
        return RagChain(embeddingEntity, chatModelEntity)
    }
}

/**
 * rag链
 */
class RagChain(
    val embeddingEntity: EmbeddingEntity,
    val chatModelEntity: ChatModelEntity,
) : ChainNode<UserMessageVal, AnswerVal> {

    private val log: Logger = LoggerFactory.getLogger(RagChain::class.java)

    override fun process(req: UserMessageVal): AnswerVal {
        val startTime = System.currentTimeMillis()

        val chain = (PreprocessedQuestionChainNode() // 预处理问题
                + ContextRetrieverChainNode(embeddingEntity) // 提取rag上下文
                + ReRangeChainNode() // 重排序
                + QuestionAnswerChainNode(chatModelEntity)) // 问题回答
        val ragContext = chain.process(req)

        // 计算耗时
        val processingTime: Long = System.currentTimeMillis() - startTime
        log.info("rag链耗时: $processingTime ms")
        return AnswerVal(
            ragContext.answer!!.message,
            ragContext.textSegmentVals!!,
            processingTime
        )
    }
}

/**
 * 预处理问题
 */
class PreprocessedQuestionChainNode : ChainNode<UserMessageVal, RagContext> {

    private val log: Logger = LoggerFactory.getLogger(PreprocessedQuestionChainNode::class.java)

    override fun process(req: UserMessageVal): RagContext {
        val startTime = System.currentTimeMillis()
        val chatModelEntity = agentArmoryFactory.chatModelEntity("chevalblanc/gpt-4o-mini", ModelSourceType.OLLAMA)
        try {
            val chatAdapter = ChatAdapterHolder.chatAdapter(chatModelEntity.sourceType)
            val chatAssistant = chatAdapter.chatAssistant(PreprocessedQuestionAssistant::class, chatModelEntity)
            val preprocessedQuestion = chatAssistant.preprocessQuestion(question = req.message)

            val processingTime: Long = System.currentTimeMillis() - startTime
            log.info("预处理问题耗时: $processingTime ms")
            return RagContext(
                question = req,
                preprocessedQuestion = preprocessedQuestion
            )
        } catch (e: Exception) {
            log.error("预处理问题失败", e)
            return RagContext(
                question = req,
                preprocessedQuestion = PreprocessedQuestion(listOf(req.message))
            )
        }
    }
}

/**
 * 提取rag上下文
 */
class ContextRetrieverChainNode(
    val embeddingEntity: EmbeddingEntity
) : ChainNode<RagContext, RagContext> {
    val contextRetriever = ContextRetrieverFactory.contextRetriever(embeddingEntity)

    private val log: Logger = LoggerFactory.getLogger(ContextRetrieverChainNode::class.java)

    override fun process(req: RagContext): RagContext {
        val startTime = System.currentTimeMillis()
        runBlocking {
            val deferredResults = req.preprocessedQuestion!!.question.map { question ->
                async { contextRetriever.retrieveContext(question, embeddingEntity.embeddingSetting.maxResults) }
            }
            val textSegmentVals = deferredResults.awaitAll().flatten()
            req.textSegmentVals = textSegmentVals
        }

        val processingTime: Long = System.currentTimeMillis() - startTime
        log.info("提取rag上下文耗时: $processingTime ms")
        return req
    }
}

/**
 * 重新排序
 */
class ReRangeChainNode(
) : ChainNode<RagContext, RagContext> {
    val documentReranker = VolcengineScoringModel()

    private val log: Logger = LoggerFactory.getLogger(ReRangeChainNode::class.java)
    override fun process(req: RagContext): RagContext {
        if (req.textSegmentVals == null) {
            log.info("重新排序: 无需重新排序, 向量匹配文本为空")
            return req
        }
        val textSegmentVals = req.textSegmentVals!!

        val startTime = System.currentTimeMillis()
        val scoringResults = documentReranker.scoreAll(textSegmentVals, req.question.message)

        val segmentVals = scoringResults.sortedByDescending { it.relevanceScore }
            .map { textSegmentVals[it.index] }
        req.textSegmentVals = segmentVals

        val processingTime: Long = System.currentTimeMillis() - startTime
        log.info("重新排序耗时: $processingTime ms")
        return req
    }
}

/**
 * 问题回答
 */
class QuestionAnswerChainNode(
    chatModelEntity: ChatModelEntity
) : ChainNode<RagContext, RagContext> {
    val answerGenerator = AnswerGeneratorFactory.answerGenerator(chatModelEntity)

    private val log: Logger = LoggerFactory.getLogger(QuestionAnswerChainNode::class.java)
    override fun process(req: RagContext): RagContext {
        val startTime = System.currentTimeMillis()
        val systemMessage = """
                你是一个智能客服, 你需要使用温和且规范化的语言, 来回答客户的问题
            """
        println("------------------")
        println(">> question: ${req.question}")
        println(">> document: ${req.textSegmentVals}")
        println("------------------")
        val answer = answerGenerator.generateAnswer(req.question.message, req.textSegmentVals!!, systemMessage)
        req.answer = AssistantMessageVal(answer)

        val processingTime: Long = System.currentTimeMillis() - startTime
        log.info("问题回答耗时: $processingTime ms")
        return req
    }
}

data class PreprocessedQuestion(
    val question: List<String>
)

data class RagContext(
    val question: UserMessageVal,
    var preprocessedQuestion: PreprocessedQuestion? = null,
    var textSegmentVals: List<TextSegmentVal>? = null,
    var answer: AssistantMessageVal? = null,
)

interface PreprocessedQuestionAssistant {
    @SystemMessage(
        """
                你是一个智能总结器, 你需要使用更加规范化的语言, 总结用户问题, 如果用户有多个想问的问题, 请拆分城多个子问题, 按照一下json格式返回, 不要输出其他内容, 只返回json:
                {
                  "question": ["用户问题1", "用户问题2"],
                }
            """
    )
    fun preprocessQuestion(question: String): PreprocessedQuestion
}