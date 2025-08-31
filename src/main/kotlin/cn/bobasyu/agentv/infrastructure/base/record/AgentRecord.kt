package cn.bobasyu.agentv.infrastructure.base.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.infrastructure.base.record.AgentRecords.TABLE_NAME
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime

object AgentRecords : Table<AgentRecord>(TABLE_NAME) {
    const val TABLE_NAME = "agent_record"
    const val ID_COLUMN = "id"
    const val CHAT_MODEL_ID = "chat_model_id"
    const val MEMORY_SAVE_FLAG = "memory_save_flag"
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(ID_COLUMN).primaryKey().bindTo { it.id }
    val chatModelId: Column<Long> = long(CHAT_MODEL_ID).bindTo { it.chatModelId }
    val memorySaveFlag: Column<Boolean> = boolean(MEMORY_SAVE_FLAG).bindTo { it.memorySaveFlag }
    val createAt: Column<LocalDateTime> = datetime(CREATE_AT_COLUMN).bindTo { it.createdAt }
    val deleteFlag: Column<Boolean> = boolean(DELETE_FLAG).bindTo { it.deleteFlag }
}

interface AgentRecord : Entity<AgentRecord> {
    companion object : Entity.Factory<AgentRecord>()

    var id: Long
    var chatModelId: Long
    var memorySaveFlag: Boolean
    var createdAt: LocalDateTime
    var deleteFlag: Boolean
}

val DatabaseHandler.agentRecords get() = this.database.sequenceOf(AgentRecords)