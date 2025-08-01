package cn.bobasyu.agentv.domain.vals

data class ChatModelConfigVal(
    var temperature: Double?,
    var maxMessage: Int?
)

enum class ChatModelSourceType {
    OLLAMA, OPENAI, VOLCENGINE
}