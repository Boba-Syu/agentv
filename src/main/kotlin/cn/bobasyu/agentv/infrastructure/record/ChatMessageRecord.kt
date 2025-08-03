package cn.bobasyu.agentv.infrastructure.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.infrastructure.record.ChatMessageRecords.TABLE_NAME
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime

object ChatMessageRecords : Table<ChatMessageRecord>(TABLE_NAME) {
    const val TABLE_NAME = "chat_message_record"
    const val ID_COLUMN = "id"
    const val AGENT_ID_COLUMN = "agent_id"
    const val MESSAGE_COLUMN = "message"
    const val ROLE_COLUMN = "role"
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(ID_COLUMN).primaryKey().bindTo { it.id }
    val agentId: Column<Long> = long(AGENT_ID_COLUMN).bindTo { it.agentId }
    val message: Column<String> = varchar(MESSAGE_COLUMN).bindTo { it.message }
    val role: Column<String> = varchar(ROLE_COLUMN).bindTo { it.role }
    val createAt: Column<LocalDateTime> = datetime(CREATE_AT_COLUMN).bindTo { it.createdAt }
    var deleteFlag: Column<Boolean> = boolean("DELETE_FLAG").bindTo { it.deleteFlag }

}

interface ChatMessageRecord : Entity<ChatMessageRecord> {
    companion object : Entity.Factory<ChatMessageRecord>()

    var id: Long
    var agentId: Long
    var message: String
    var role: String
    var createdAt: LocalDateTime
    var deleteFlag: Boolean
}

val DatabaseHandler.chatMessageRecords get() = this.database.sequenceOf(ChatMessageRecords)