package cn.bobasyu.agentv.infrastructure.base.repository.command.message

import cn.bobasyu.agentv.domain.base.repository.comand.AgentRepository
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.SystemMessageVal
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import cn.bobasyu.agentv.infrastructure.base.converter.toLangChain4jMessage
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ChatMessageType
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.TextContent
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.store.memory.chat.ChatMemoryStore

class PersistentChatMemoryStore(
    private val agentRepository: AgentRepository
) : ChatMemoryStore {
    override fun getMessages(memoryId: Any): List<ChatMessage> {
        val chatModelId = memoryId as ChatModelId
        return agentRepository.query().findMessages(AgentId(chatModelId.value))
            .map { toLangChain4jMessage(it) }
    }

    override fun updateMessages(memoryId: Any, chetMessages: List<ChatMessage>) {
        val chatModelId = memoryId as ChatModelId
        val messageVals: List<MessageVal> = chetMessages.map { chatMessage ->
            val text: String = when (chatMessage) {
                is SystemMessage -> chatMessage.text()
                is UserMessage -> (chatMessage.contents().first() as TextContent).text()
                is AiMessage -> chatMessage.text()
                else -> throw IllegalArgumentException("Invalid role: ${chatMessage.type()}")
            }
            when (chatMessage.type()) {
                ChatMessageType.USER -> UserMessageVal(text)
                ChatMessageType.AI -> AssistantMessageVal(text)
                ChatMessageType.SYSTEM -> SystemMessageVal(text)
                else -> throw IllegalArgumentException("Invalid role: ${chatMessage.type()}")
            }
        }
        agentRepository.saveMessages(AgentId(chatModelId.value), messageVals)
    }

    override fun deleteMessages(memoryId: Any) {
        val agentId = memoryId as Long
        agentRepository.deleteMessages(AgentId(agentId))
    }
}