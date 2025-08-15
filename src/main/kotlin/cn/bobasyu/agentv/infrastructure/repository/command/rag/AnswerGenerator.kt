package cn.bobasyu.agentv.infrastructure.repository.command.rag

import dev.langchain4j.data.segment.TextSegment


/**
 * 答案生成器接口：抽象大语言模型调用
 */
internal interface AnswerGenerator {
    /**
     * 根据问题和上下文生成答案
     * @param question 用户问题
     * @param context 相关上下文片段
     * @return 生成的答案文本
     */
    fun generateAnswer(question: String, context: MutableList<TextSegment>): String

    /**
     * 带系统提示的答案生成
     * @param systemPrompt 系统级指令
     * @param question 用户问题
     * @param context 相关上下文片段
     * @return 生成的答案
     */
    fun generateAnswerWithPrompt(systemPrompt: String, question: String, context: MutableList<TextSegment>): String
}