package cn.bobasyu.agentv.domain.repository.query

import cn.bobasyu.agentv.common.repository.BaseQueryRepository
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.vals.*

interface AgentQueryRepository : BaseQueryRepository {

    fun findAgentEntity(agentId: AgentId): AgentEntity

    fun findChatModelEntity(chatModelId: ChatModelId): ChatModelEntity

    fun findMcpEntity(mcpId: McpId): McpEntity

    fun listMcpEntities(mcpIds: List<McpId>): List<McpEntity>

    fun findEmbeddingModelEntity(embeddingModelId: EmbeddingModelId): EmbeddingModelEntity

    fun findMessages(agentId: AgentId): List<MessageVal>
}