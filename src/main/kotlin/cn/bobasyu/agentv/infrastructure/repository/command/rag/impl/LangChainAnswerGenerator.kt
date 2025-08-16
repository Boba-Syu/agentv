package cn.bobasyu.agentv.infrastructure.repository.command.rag.impl

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.converter.toOllamaChatModel
import cn.bobasyu.agentv.infrastructure.repository.command.rag.AnswerGenerator


class LangChainAnswerGenerator(
    val chatModelEntity: ChatModelEntity
) : AnswerGenerator {
    val chatModel = toOllamaChatModel(chatModelEntity)

    override fun generateAnswer(question: String, context: List<TextSegmentVal>, systemPrompt: String?): String {
        // 构建上下文字符串
        val contextText: String = context.joinToString("\n\n") { it.text }
        // 构造系统提示
        val prompt: String = (systemPrompt ?: chatModelEntity.role?.content ?: "")
            .replace("{context}", contextText)
        // LangChain4j的对话接口
        return chatModel.chat("$prompt\n\n用户问题: $question")
    }
}