package cn.bobasyu.agentv.infrastructure.base.repository.command

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.domain.base.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.base.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.base.repository.comand.AgentRepository
import cn.bobasyu.agentv.domain.base.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.base.vals.*
import cn.bobasyu.agentv.infrastructure.base.record.AgentRecords
import cn.bobasyu.agentv.infrastructure.base.record.ChatMessageRecords
import cn.bobasyu.agentv.infrastructure.base.record.ChatModelRecords
import cn.bobasyu.agentv.infrastructure.base.repository.command.chat.ChatAdapterHolder
import org.ktorm.dsl.*
import java.time.LocalDateTime


class AgentRepositoryImpl(
    private val agentQueryRepository: AgentQueryRepository,
    private val databaseHandler: DatabaseHandler,
) : AgentRepository {

    override fun chat(
        agentAggregate: AgentAggregate, message: UserMessageVal
    ): AssistantMessageVal {
        val chatAdapter = ChatAdapterHolder.chatAdapter(agentAggregate.chatModel.sourceType)
        return chatAdapter.chat(agentAggregate, message)
    }

    override fun chat(chatModel: ChatModelEntity, message: UserMessageVal): AssistantMessageVal {
        val chatAdapter = ChatAdapterHolder.chatAdapter(chatModel.sourceType)
        return chatAdapter.chat(chatModel, message)
    }

    override fun saveModel(chatModel: ChatModelEntity) {
        if (query().chatModelEntityExist(chatModel.id)) {
            databaseHandler.update(ChatModelRecords) {
                where { it.id eq chatModel.id.value }
                set(it.modelName, chatModel.modelName)
                set(it.role, chatModel.role?.message)
                set(it.sourceType, chatModel.sourceType.name)
            }
        } else {
            databaseHandler.insert(ChatModelRecords) {
                set(it.id, chatModel.id.value)
                set(it.modelName, chatModel.modelName)
                set(it.role, chatModel.role?.message)
                set(it.sourceType, chatModel.sourceType.name)
                set(it.createAt, LocalDateTime.now())
                set(it.deleteFlag, false)
            }
        }
    }

    override fun saveMessages(
        agentId: AgentId,
        messages: List<MessageVal>
    ) {
        databaseHandler.batchInsert(ChatMessageRecords) {
            messages.map { chatMessage ->
                item {
                    set(it.id, generateId())
                    set(ChatMessageRecords.agentId, agentId.value)
                    set(ChatMessageRecords.message, chatMessage.message)
                    set(ChatMessageRecords.role, chatMessage.role.name.lowercase())
                    set(ChatMessageRecords.createAt, LocalDateTime.now())
                    set(ChatMessageRecords.deleteFlag, false)
                }
            }
        }
    }

    override fun deleteMessages(agentId: AgentId) {
        val messageRecords = databaseHandler.from(ChatMessageRecords)
            .select()
            .apply {
                where { ChatMessageRecords.agentId eq agentId.value }
                where { ChatMessageRecords.deleteFlag eq false }
            }.orderBy(ChatMessageRecords.createAt.asc())
            .map { row -> ChatMessageRecords.createEntity(row) }

        databaseHandler.batchUpdate(ChatMessageRecords) {
            messageRecords.map { messageRecord ->
                item {
                    set(it.id, messageRecord.id)
                    set(ChatMessageRecords.deleteFlag, false)
                }
            }
        }
    }

    override fun createNewAgent(chatModelId: ChatModelId, mcpIds: List<McpId>): AgentId {
        val agentId = generateId()
        databaseHandler.insert(AgentRecords) {
            set(it.id, agentId)
            set(it.chatModelId, chatModelId.value)
        }
        return AgentId(agentId)
    }

    override fun query() = agentQueryRepository
}