package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaChatModel
import cn.bobasyu.agentv.infrastructure.base.converter.toOllamaStreamingChatModel
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.AnswerGenerator
import dev.langchain4j.data.message.UserMessage
import kotlinx.coroutines.runBlocking
import me.kpavlov.langchain4j.kotlin.model.chat.StreamingChatModelReply
import me.kpavlov.langchain4j.kotlin.model.chat.chatFlow


class LangChainAnswerGenerator(
    val chatModelEntity: ChatModelEntity
) : AnswerGenerator {
    val streamChatModel = toOllamaStreamingChatModel(chatModelEntity)
    val chatModel = toOllamaChatModel(chatModelEntity)

    override fun generateAnswer(question: String, context: List<TextSegmentVal>, systemPrompt: String?): String {
        // 构建上下文字符串
        val contextText: String = context.joinToString("\n\n") { it.text }
        // 构造系统提示
        val userMessage: UserMessage = buildSystemMessage(systemPrompt, contextText, question)
        // LangChain4j的对话接口
        return chat(userMessage)
    }

    private fun chat(userMessage: UserMessage): String = with(StringBuilder()) {
        runBlocking {
            streamChatModel.chatFlow {
                messages += userMessage
            }.collect { reply ->
                when (reply) {
                    is StreamingChatModelReply.PartialResponse -> append(reply.token)
                    is StreamingChatModelReply.CompleteResponse -> Unit
                }
            }
        }
        toString()
    }

    private fun buildSystemMessage(systemPrompt: String?, contextText: String, question: String): UserMessage {
        val ragPrompt = """
                ${systemPrompt ?: chatModelEntity.role?.message ?: ""}
                请参考一下上下文, 基于上下文回答用户问题, 不要回复用户问题以外的内容, 避免出现错误。
                $contextText
            """.trimIndent()
        return UserMessage("$ragPrompt\n\n用户问题: $question")
    }
}