package cn.bobasyu.agentv.infrastructure.base.repository.query

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.base.entity.AgentEntity
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.entity.McpEntity
import cn.bobasyu.agentv.domain.base.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.base.vals.*
import cn.bobasyu.agentv.infrastructure.base.record.*
import org.ktorm.dsl.*
import org.ktorm.entity.count
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
            memorySaveFlag = agentRecord.memorySaveFlag
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
            sourceType = modelSourceType(chatModelRecord.sourceType)
        )
    }

    override fun chatModelEntityExist(chatModelId: ChatModelId): Boolean {
        val count = databaseHandler.chatModelRecords.count {
            it.id eq chatModelId.value
        }
        return count > 0
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
        if (mcpIds.isEmpty()) {
            return emptyList()
        }
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

    override fun findEmbeddingModelEntity(ragId: RagId): EmbeddingEntity {
        val embeddingRecord = databaseHandler.embeddingRecords.find {
            it.id eq ragId.value
        }
        if (embeddingRecord == null) {
            throw Exception("embeddingRecord not found")
        }
        return EmbeddingEntity(
            id = RagId(embeddingRecord.id),
            modelName = embeddingRecord.modelName,
            embeddingSetting = embeddingRecord.config,
            sourceType = modelSourceType(embeddingRecord.sourceType),
            dimension = embeddingRecord.dimension
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
}