package cn.bobasyu.agentv.infrastructure.repository.query

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.entity.RagEntity
import cn.bobasyu.agentv.domain.entity.ToolEntity
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.AssistantMessageVal
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.MessageRole
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.RagId
import cn.bobasyu.agentv.domain.vals.SystemMessageVal
import cn.bobasyu.agentv.domain.vals.ToolId
import cn.bobasyu.agentv.domain.vals.UserMessageVal
import cn.bobasyu.agentv.domain.vals.chatModelSourceType
import cn.bobasyu.agentv.domain.vals.mcpTransportType
import cn.bobasyu.agentv.domain.vals.messageRole
import cn.bobasyu.agentv.infrastructure.record.AgentRecord
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecords
import cn.bobasyu.agentv.infrastructure.record.McpRecords
import cn.bobasyu.agentv.infrastructure.record.agentRecords
import cn.bobasyu.agentv.infrastructure.record.chatModelRecords
import cn.bobasyu.agentv.infrastructure.record.embeddingRecords
import cn.bobasyu.agentv.infrastructure.record.mcpRecords
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.inList
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.find

class AgentQueryRepositoryImpl(
    private val databaseHandler: DatabaseHandler
) : AgentQueryRepository {

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
            mcpIdList = agentRecord.mcp.map { McpId(it) }.toMutableList(),
            toolIdList = agentRecord.tools.map { ToolId(it) }.toMutableList()
        )
    }

    override fun findChatModelEntity(chatModelId: ChatModelId): ChatModelEntity {
        val chatModelRecord = databaseHandler.chatModelRecords.find {
            it.id eq chatModelId.value
        }
        if (chatModelRecord == null) {
            throw Exception("ChatModelRecord not found")
        }

        return ChatModelEntity(
            id = ChatModelId(chatModelRecord.id),
            modelName = chatModelRecord.modelName,
            role = if (chatModelRecord.role == null) null else SystemMessageVal(chatModelRecord.role!!),
            config = chatModelRecord.config,
            sourceType = chatModelSourceType(chatModelRecord.sourceType)
        )
    }

    override fun findMcpEntity(mcpId: McpId): McpEntity {
        val chatModelRecord = databaseHandler.mcpRecords.find {
            it.id eq mcpId.value
        }
        if (chatModelRecord == null) {
            throw Exception("ChatModelRecord not found")
        }
        return McpEntity(
            id = McpId(chatModelRecord.id),
            name = chatModelRecord.name,
            type = mcpTransportType(chatModelRecord.type),
            config = chatModelRecord.config
        )
    }

    override fun listMcpEntities(mcpIds: List<McpId>): List<McpEntity> {
        return databaseHandler.from(McpRecords)
            .select()
            .apply {
                where { McpRecords.id inList mcpIds.map { mcp -> mcp.value } }
                where { McpRecords.deleteFlag eq false }
            }.orderBy(ChatMessageRecords.createAt.asc())
            .map { row -> McpRecords.createEntity(row) }
            .map {
                McpEntity(
                    id = McpId(it.id),
                    name = it.name,
                    type = mcpTransportType(it.type),
                    config = it.config
                )
            }
    }

    override fun findEmbeddingModelEntity(ragId: RagId): RagEntity {
        val embeddingRecord = databaseHandler.embeddingRecords.find {
            it.id eq ragId.value
        }
        if (embeddingRecord == null) {
            throw Exception("embeddingRecord not found")
        }
        return RagEntity(
            id = RagId(embeddingRecord.id),
            model = embeddingRecord.modelName,
            embeddingSetting = embeddingRecord.config
        )

    }

    override fun findMessages(agentId: AgentId): List<MessageVal> {
        return databaseHandler.from(ChatMessageRecords)
            .select()
            .apply {
                where { ChatMessageRecords.agentId eq agentId.value }
                where { ChatMessageRecords.deleteFlag eq false }
            }.orderBy(ChatMessageRecords.createAt.asc())
            .map { row -> ChatMessageRecords.createEntity(row) }
            .map {
                when (messageRole(it.role)) {
                    MessageRole.USER -> UserMessageVal(it.message)
                    MessageRole.ASSISTANT -> AssistantMessageVal(it.message)
                    MessageRole.SYSTEM -> SystemMessageVal(it.message)
                }
            }
    }

    override fun listToolEntities(toolIdList: MutableList<ToolId>): List<ToolEntity> {
        TODO("Not yet implemented")
    }
}