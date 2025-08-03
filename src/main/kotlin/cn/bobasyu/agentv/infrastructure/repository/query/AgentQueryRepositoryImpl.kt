package cn.bobasyu.agentv.infrastructure.repository.query

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.config.ApplicationContext
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.EmbeddingModelId
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.infrastructure.record.AgentRecord
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecord
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecords
import cn.bobasyu.agentv.infrastructure.record.agentRecords
import org.ktorm.dsl.*
import org.ktorm.entity.find

class AgentQueryRepositoryImpl(
    private val applicationContext: ApplicationContext,
) : AgentQueryRepository {
    private val databaseHandler get() = applicationContext.databaseHandler

    override fun findAgentEntity(agentId: AgentId): AgentEntity {
        val agentRecord: AgentRecord? = databaseHandler.agentRecords.find {
            it.id eq agentId.value
        }
        if (agentRecord == null) {
            throw Exception("Agent not found, id=${agentId.value}")
        }
        return AgentEntity(
            id = AgentId(agentRecord.id),
            chatModelId = ChatModelId(agentRecord.chatModelId),
            mcpIdList = agentRecord.mcp.map { McpId(it) }.toMutableList()
        )
    }

    override fun findChatModelEntity(chatModelId: ChatModelId): ChatModelEntity {
        TODO("Not yet implemented")
    }

    override fun findMcpEntity(mcpId: McpId): McpEntity {
        TODO("Not yet implemented")
    }

    override fun listMcpEntities(mcpIds: List<McpId>): List<McpEntity> {
        TODO("Not yet implemented")
    }

    override fun findEmbeddingModelEntity(embeddingModelId: EmbeddingModelId): EmbeddingModelEntity {
        TODO("Not yet implemented")
    }

    override fun findMessages(agentId: AgentId): List<ChatMessageRecord> {
        return databaseHandler.from(ChatMessageRecords)
            .select()
            .apply {
                where { ChatMessageRecords.agentId eq agentId.value }
                where { ChatMessageRecords.deleteFlag eq false }
            }.orderBy(ChatMessageRecords.createAt.asc())
            .map { row -> ChatMessageRecords.createEntity(row) }
    }
}