package cn.bobasyu.agentv.domain.vals


enum class MessageRole {
    SYSTEM, USER, ASSISTANT
}

sealed class MessageVal(
    val role: MessageRole,
    val content: String
)

class SystemMessageVal(message: String) : MessageVal(MessageRole.SYSTEM, message)

class UserMessageVal(message: String) : MessageVal(MessageRole.USER, message)

class AssistantMessageVal(message: String) : MessageVal(MessageRole.ASSISTANT, message)
