package cn.bobasyu.agentv.infrastructure.base.repository.command.rag

import cn.bobasyu.agentv.application.service.AgentServices.Factory.agentArmoryFactory
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.*
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.ChatAdapterHolder
import dev.langchain4j.service.SystemMessage

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

    override fun process(req: UserMessageVal): AnswerVal {
        val startTime = System.currentTimeMillis()

        val chain = (PreprocessedQuestionChainNode() // 预处理问题
                + ContextRetrieverChainNode(embeddingEntity) // 提取rag上下文
                + QuestionAnswerChainNode(chatModelEntity)) // 问题回答
        val ragContext = chain.process(req)

        // 计算耗时
        val processingTime: Long = System.currentTimeMillis() - startTime
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
    override fun process(req: UserMessageVal): RagContext {
        val chatModelEntity = agentArmoryFactory.chatModelEntity("chevalblanc/gpt-4o-mini", ModelSourceType.OLLAMA)
        try {
            val chatAdapter = ChatAdapterHolder.chatAdapter(chatModelEntity.sourceType)
            val chatAssistant = chatAdapter.chatAssistant(PreprocessedQuestionAssistant::class, chatModelEntity)
            val preprocessedQuestion = chatAssistant.preprocessQuestion(question =  req.message)
            return RagContext(
                question = req,
                preprocessedQuestion = preprocessedQuestion
            )
        } catch (e: Exception) {
            println("预处理问题失败: $e")
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

    override fun process(req: RagContext): RagContext {
        val textSegmentVals: List<TextSegmentVal> = req.preprocessedQuestion!!.question.map { question ->
            contextRetriever.retrieveContext(question, embeddingEntity.embeddingSetting.maxResults)
        }.flatMap { it }
        req.textSegmentVals = textSegmentVals
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
    override fun process(req: RagContext): RagContext {
        val answer = answerGenerator.generateAnswer(req.question.message, req.textSegmentVals!!)
        req.answer = AssistantMessageVal(answer)
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
    @SystemMessage("""
                你是一个智能总结器, 你需要使用更加规范化的语言, 总结用户问题, 如果用户有多个想问的问题, 请拆分城多个子问题, 按照一下json格式返回, 不要输出其他内容, 只返回json:
                {
                  "question": ["用户问题1", "用户问题2"],
                }
            """)
    fun preprocessQuestion(question: String): PreprocessedQuestion
}