package cn.bobasyu.agentv.domain.vals

import cn.bobasyu.agentv.common.vals.Id

class ChatModelId(override val id: Long) : Id(id)

data class EmbeddingModelId(override val id: Long) : Id(id)

data class ChatId(override val id: Long) : Id(id)

data class McpId(override val id: Long) : Id(id)


fun main() {
    val chatId = ChatId(1)
    println(chatId)
}