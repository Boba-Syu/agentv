package cn.bobasyu.agentv.infrastructure.base.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.base.vals.McpConfigVal
import cn.bobasyu.agentv.infrastructure.base.record.McpRecords.TABLE_NAME
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
    const val TABLE_NAME = "mcp_record"  // 修复：使用独立的表名
    const val ID_COLUMN = "id"
    const val NAME_COLUMN = "name"
    const val TYPE_COLUMN = "type"
    const val CONFIG_COLUMN = "config"   // 添加：config列常量
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(ID_COLUMN).primaryKey().bindTo { it.id }
    val name: Column<String> = varchar(NAME_COLUMN).bindTo { it.name }  // 修复：使用正确的常量
    val type: Column<String> = varchar(TYPE_COLUMN).bindTo { it.type }  // 修复：使用正确的常量
    val config: Column<McpConfigVal> = json<McpConfigVal>(CONFIG_COLUMN).bindTo { it.config }  // 修复：使用正确的常量
    val createAt: Column<LocalDateTime> = datetime(CREATE_AT_COLUMN).bindTo { it.createdAt }  // 修复：使用正确的常量
    val deleteFlag: Column<Boolean> = boolean(DELETE_FLAG).bindTo { it.deleteFlag }  // 修复：使用正确的常量
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