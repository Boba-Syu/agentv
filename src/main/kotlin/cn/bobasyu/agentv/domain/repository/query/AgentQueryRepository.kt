package cn.bobasyu.agentv.domain.repository.query

import cn.bobasyu.agentv.common.repository.BaseQueryRepository
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.EmbeddingModelId
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecord

interface AgentQueryRepository : BaseQueryRepository {

    fun findAgentEntity(agentId: AgentId): AgentEntity

    fun findChatModelEntity(chatModelId: ChatModelId): ChatModelEntity

    fun findMcpEntity(mcpId: McpId): McpEntity

    fun listMcpEntities(mcpIds: List<McpId>): List<McpEntity>

    fun findEmbeddingModelEntity(embeddingModelId: EmbeddingModelId): EmbeddingModelEntity

    fun findMessages(agentId: AgentId): List<ChatMessageRecord>
}