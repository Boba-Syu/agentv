package cn.bobasyu.agentv.domain.repository.query

import cn.bobasyu.agentv.common.repository.BaseQueryRepository
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.entity.ToolEntity
import cn.bobasyu.agentv.domain.vals.*

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

    /**
     * 查询工具
     */
    fun listToolEntities(toolIdList: MutableList<ToolId>): List<ToolEntity>
}

