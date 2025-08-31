package cn.bobasyu.agentv.domain.base.repository.query

import cn.bobasyu.agentv.common.repository.BaseQueryRepository
import cn.bobasyu.agentv.domain.base.entity.AgentEntity
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.vals.*

interface AgentQueryRepository : BaseQueryRepository {

    /**
     * 查询Agent
     */
    fun findAgentEntity(agentId: AgentId): AgentEntity

    /**
     * 查询ChatModel
     */
    fun findChatModelEntity(chatModelId: ChatModelId): ChatModelEntity
    /**
     * 查询ChatModel是否保存
     */
    fun chatModelEntityExist(chatModelId: ChatModelId): Boolean

    /**
     * 查询Mcp
     */
    fun findMcpEntity(mcpId: McpId): McpEntity

    /**
     * 查询Mcp
     */
    fun listMcpEntities(mcpIds: List<McpId>): List<McpEntity>

    /**
     * 查询EmbeddingModel
     */
    fun findEmbeddingModelEntity(ragId: RagId): EmbeddingEntity

    /**
     * 查询消息
     */
    fun findMessages(agentId: AgentId): List<MessageVal>
}

