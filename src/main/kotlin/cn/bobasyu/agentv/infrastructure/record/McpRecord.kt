package cn.bobasyu.agentv.infrastructure.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.infrastructure.record.McpRecords.TABLE_NAME
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

object McpRecords : Table<McpRecord>(TABLE_NAME) {
    const val TABLE_NAME = "agent_record"
    const val ID_COLUMN = "id"
    const val NAME_COLUMN = "name"
    const val TYPE_COLUMN = "type"
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(AgentRecords.ID_COLUMN).primaryKey().bindTo { it.id }
    val name: Column<String> = varchar(AgentRecords.ID_COLUMN).bindTo { it.name }
    val type: Column<String> = varchar(AgentRecords.ID_COLUMN).bindTo { it.type }
    val config: Column<McpConfigVal> = json<McpConfigVal>(AgentRecords.ID_COLUMN).bindTo { it.config }
    val createAt: Column<LocalDateTime> = datetime(AgentRecords.CREATE_AT_COLUMN).bindTo { it.createdAt }
    val deleteFlag: Column<Boolean> = boolean(AgentRecords.DELETE_FLAG).bindTo { it.deleteFlag }

}

interface McpRecord : Entity<McpRecord> {
    companion object : Entity.Factory<McpRecord>()

    var id: Long
    var name: String
    var type: String
    var config: McpConfigVal
    var createdAt: LocalDateTime
    var deleteFlag: Boolean
}

val DatabaseHandler.mcpRecords get() = this.database.sequenceOf(McpRecords)