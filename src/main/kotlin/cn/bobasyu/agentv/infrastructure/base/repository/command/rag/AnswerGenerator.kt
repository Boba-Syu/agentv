package cn.bobasyu.agentv.infrastructure.base.repository.command.rag

import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.LangChainAnswerGenerator


/**
 * 答案生成器接口：抽象大语言模型调用
 */
interface AnswerGenerator {
    /**
     * 根据问题和上下文生成答案
     * @param question 用户问题
     * @param context 相关上下文片段
     * @param systemPrompt 系统级指令
     * @return 生成的答案文本
     */
    fun generateAnswer(question: String, context: List<TextSegmentVal>, systemPrompt: String? = null): String
}

object AnswerGeneratorFactory {

    fun answerGenerator(chatModelEntity: ChatModelEntity): AnswerGenerator = when (chatModelEntity.sourceType) {
        ModelSourceType.OLLAMA -> LangChainAnswerGenerator(chatModelEntity)
        else -> TODO()
    }
}