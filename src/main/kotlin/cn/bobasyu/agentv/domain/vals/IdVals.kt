package cn.bobasyu.agentv.domain.vals

import cn.bobasyu.agentv.common.vals.Id

class ChatModelId(override val value: Long) : Id(value)

data class EmbeddingModelId(override val value: Long) : Id(value)

data class AgentId(override val value: Long) : Id(value)

data class McpId(override val value: Long) : Id(value)