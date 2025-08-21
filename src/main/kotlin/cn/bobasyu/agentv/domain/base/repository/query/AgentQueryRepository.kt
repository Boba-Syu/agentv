package cn.bobasyu.agentv.domain.base.repository.query

import cn.bobasyu.agentv.common.repository.BaseQueryRepository
import cn.bobasyu.agentv.domain.base.entity.AgentEntity
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.AgentId
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.McpId
import cn.bobasyu.agentv.domain.base.vals.MessageVal
import cn.bobasyu.agentv.domain.base.vals.RagId
import cn.bobasyu.agentv.domain.base.vals.ToolId

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

