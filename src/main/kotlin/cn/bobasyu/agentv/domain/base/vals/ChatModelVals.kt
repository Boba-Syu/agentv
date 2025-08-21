package cn.bobasyu.agentv.domain.base.vals

data class ChatModelConfigVal(
    var temperature: Double?,
    var maxMessage: Int?
)

enum class ModelSourceType {
    OLLAMA, OPENAI, VOLCENGINE
}

fun modelSourceType(str: String): ModelSourceType {
    for (value in ModelSourceType.entries) {
        if (value.name == str.uppercase()) {
            return value
        }
    }
    throw IllegalArgumentException("Invalid ChatModelSourceType: $str")
}