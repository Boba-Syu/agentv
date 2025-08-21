package cn.bobasyu.agentv.domain.base.vals

import cn.bobasyu.agentv.common.vals.Id

class ChatModelId(override val value: Long) : Id(value)

data class RagId(override val value: Long) : Id(value)

data class AgentId(override val value: Long) : Id(value)

data class McpId(override val value: Long) : Id(value)

data class ToolId(override val value: Long) : Id(value)

data class WorkflowId(override val value: Long) : Id(value)

data class DocumentId(override val value: Long) : Id(value)