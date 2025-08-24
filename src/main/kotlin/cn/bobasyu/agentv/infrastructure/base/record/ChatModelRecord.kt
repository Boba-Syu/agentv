package cn.bobasyu.agentv.infrastructure.base.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.base.vals.ChatModelConfigVal
import cn.bobasyu.agentv.infrastructure.base.record.ChatModelRecords.TABLE_NAME
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.jackson.json
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime

object ChatModelRecords: Table<ChatModelRecord>(TABLE_NAME) {
    const val TABLE_NAME = "chat_model_record"
    const val ID_COLUMN = "id"
    const val MODEL_MANE_COLUMN = "model_name"
    const val ROLE_COLUMN = "role"
    const val SOURCE_TYPE_COLUMN = "source_type"
    const val CONFIG_COLUMN = "config"
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(ID_COLUMN).primaryKey().bindTo { it.id }
    val modelName: Column<String> = varchar(MODEL_MANE_COLUMN).bindTo { it.modelName }
    val role: Column<String> = varchar(ROLE_COLUMN).bindTo { it.role }
    val sourceType: Column<String> = varchar(SOURCE_TYPE_COLUMN).bindTo { it.sourceType }
    val config: Column<ChatModelConfigVal> = json<ChatModelConfigVal>(CONFIG_COLUMN).bindTo { it.config }
    val createAt: Column<LocalDateTime> = datetime(CREATE_AT_COLUMN).bindTo { it.createdAt }
    val deleteFlag: Column<Boolean> = boolean(DELETE_FLAG).bindTo { it.deleteFlag }
}


interface ChatModelRecord : Entity<ChatModelRecord> {
    companion object : Entity.Factory<ChatModelRecord>()

    var id: Long
    var modelName: String
    var role: String?
    var sourceType: String
    var config: ChatModelConfigVal?
    var createdAt: LocalDateTime
    var deleteFlag: Boolean
}

val DatabaseHandler.chatModelRecords get() = this.database.sequenceOf(ChatModelRecords)