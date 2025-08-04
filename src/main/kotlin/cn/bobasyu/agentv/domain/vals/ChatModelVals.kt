package cn.bobasyu.agentv.domain.vals

data class ChatModelConfigVal(
    var temperature: Double?,
    var maxMessage: Int?
)

enum class ChatModelSourceType {
    OLLAMA, OPENAI, VOLCENGINE
}

fun chatModelSourceType(str: String): ChatModelSourceType = when (str.lowercase()) {
        "ollama" -> ChatModelSourceType.OLLAMA
        "openai" -> ChatModelSourceType.OPENAI
        "volcengine" -> ChatModelSourceType.VOLCENGINE
        else -> throw IllegalArgumentException("Invalid chat model source type: $str")
    }
