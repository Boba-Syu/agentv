package cn.bobasyu.agentv.domain.base.vals


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
    open val message: String
)

data class SystemMessageVal(override val message: String) : MessageVal(MessageRole.SYSTEM, message)

data class UserMessageVal(override val message: String) : MessageVal(MessageRole.USER, message)

data class AssistantMessageVal(override val message: String) : MessageVal(MessageRole.ASSISTANT, message)
