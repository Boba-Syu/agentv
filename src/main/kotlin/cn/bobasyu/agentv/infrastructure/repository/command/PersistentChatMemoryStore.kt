package cn.bobasyu.agentv.infrastructure.repository.command

import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.vals.*
import cn.bobasyu.agentv.infrastructure.converter.langChain4jMessage
import dev.langchain4j.data.message.*
import dev.langchain4j.store.memory.chat.ChatMemoryStore

class PersistentChatMemoryStore(
    private val agentRepository: AgentCommandRepository
) : ChatMemoryStore {
    override fun getMessages(memoryId: Any): List<ChatMessage> {
        val agentId = memoryId as Long
        return agentRepository.query().findMessages(AgentId(agentId))
            .map {
                when (it.role) {
                    "user" -> langChain4jMessage(UserMessageVal(it.message))
                    "assistant" -> langChain4jMessage(AssistantMessageVal(it.message))
                    "system" -> langChain4jMessage(SystemMessageVal(it.message))
                    else -> throw IllegalArgumentException("Invalid role: ${it.role}")
                }
            }
    }

    override fun updateMessages(memoryId: Any, chetMessages: List<ChatMessage>) {
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
        agentRepository.updateMessages(AgentId(memoryId as Long), messageVals)
    }

    override fun deleteMessages(memoryId: Any) {
        val agentId = memoryId as Long
        agentRepository.deleteMessages(AgentId(agentId))
    }
}