package cn.bobasyu.agentv.domain.vals


enum class MessageRole {
    SYSTEM, USER, ASSISTANT
}

fun messageRole(role: String): MessageRole {
    for (value in MessageRole.entries) {
        if (value.name == role.uppercase()) {
            return value
        }
    }
    throw IllegalArgumentException("Invalid message role: $role")
}

sealed class MessageVal(
    val role: MessageRole,
    val content: String
)

class SystemMessageVal(message: String) : MessageVal(MessageRole.SYSTEM, message)

class UserMessageVal(message: String) : MessageVal(MessageRole.USER, message)

class AssistantMessageVal(message: String) : MessageVal(MessageRole.ASSISTANT, message)
