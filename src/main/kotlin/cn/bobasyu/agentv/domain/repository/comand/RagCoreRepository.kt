package cn.bobasyu.agentv.domain.repository.comand

import cn.bobasyu.agentv.domain.vals.AnswerVal
import cn.bobasyu.agentv.domain.vals.MetadataFilter

/**
 * RAG核心服务：协调检索和生成过程
 */
internal interface RagCoreRepository {
    /**
     * 问答服务核心方法
     * @param question 用户问题
     * @return 完整答案对象（含支撑证据）
     */
    fun answerQuestion(question: String): AnswerVal

    /**
     * 带元数据过滤的问答
     * @param question 用户问题
     * @param filter 元数据过滤器
     * @return 过滤后的答案
     */
    fun answerQuestionWithFilter(question: String, filter: MetadataFilter): AnswerVal

    /**
     * 带自定义提示的问答
     * @param systemPrompt 系统提示
     * @param question 用户问题
     * @return 定制化答案
     */
    fun answerWithCustomPrompt(systemPrompt: String, question: String): AnswerVal
}