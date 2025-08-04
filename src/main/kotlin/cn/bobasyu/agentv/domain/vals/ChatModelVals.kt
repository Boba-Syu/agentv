package cn.bobasyu.agentv.domain.vals

data class ChatModelConfigVal(
    var temperature: Double?,
    var maxMessage: Int?
)

enum class ChatModelSourceType {
    OLLAMA, OPENAI, VOLCENGINE
}

fun chatModelSourceType(str: String): ChatModelSourceType {
    for (value in ChatModelSourceType.entries) {
        if (value.name == str.uppercase()) {
            return value
        }
    }
    throw IllegalArgumentException("Invalid ChatModelSourceType: $str")
}