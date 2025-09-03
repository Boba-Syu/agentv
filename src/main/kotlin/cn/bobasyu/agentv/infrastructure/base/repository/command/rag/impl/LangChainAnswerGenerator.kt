package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaStreamingChatModel
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.AnswerGenerator
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler


class LangChainAnswerGenerator(
    val chatModelEntity: ChatModelEntity
) : AnswerGenerator {
    val chatModel = toOllamaStreamingChatModel(chatModelEntity)

    override fun generateAnswer(question: String, context: List<TextSegmentVal>, systemPrompt: String?): String {
        // 构建上下文字符串
        val contextText: String = context.joinToString("\n\n") { it.text }
        // 构造系统提示
        val prompt: String = """
            ${systemPrompt ?: chatModelEntity.role?.message ?: ""}
            请参考一下上下文, 基于上下文回答用户问题, 不要回复用户问题以外的内容, 避免出现错误。
            $contextText
        """.trimIndent()
        // LangChain4j的对话接口
        return with(StringBuilder()) {
            chatModel.chat("$prompt\n\n用户问题: $question", RagAnswerStreamingChatResponseHandler(this))
            toString()
        }
    }
}

class RagAnswerStreamingChatResponseHandler(
    val sb: StringBuilder
) : StreamingChatResponseHandler {
    override fun onPartialResponse(partialResponse: String?) {
        sb.append(partialResponse ?: "")
    }

    override fun onCompleteResponse(completeResponse: ChatResponse?) = Unit
    override fun onError(error: Throwable) {
        throw error
    }
}