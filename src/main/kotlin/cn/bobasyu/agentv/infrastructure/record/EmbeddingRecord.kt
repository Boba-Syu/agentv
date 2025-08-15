package cn.bobasyu.agentv.infrastructure.record

import cn.bobasyu.agentv.common.repository.DatabaseHandler
import cn.bobasyu.agentv.domain.vals.EmbeddingConfigVal
import cn.bobasyu.agentv.infrastructure.record.EmbeddingRecords.TABLE_NAME
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.jackson.json
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime

object EmbeddingRecords : Table<EmbeddingRecord>(TABLE_NAME) {
    const val TABLE_NAME = "chat_model_record"
    const val ID_COLUMN = "id"
    const val MODEL_MANE_COLUMN = "model_name"
    const val CONFIG_COLUMN = "config"
    const val SOURCE_TYPE_COLUMN = "source_type"
    const val DIMENSION_COLUMN = "dimension"
    const val CREATE_AT_COLUMN = "create_at"
    const val DELETE_FLAG = "delete_flag"

    val id: Column<Long> = long(ID_COLUMN).primaryKey().bindTo { it.id }
    val modelName: Column<String> = varchar(MODEL_MANE_COLUMN).bindTo { it.modelName }
    val config: Column<EmbeddingConfigVal> = json<EmbeddingConfigVal>(CONFIG_COLUMN).bindTo { it.config }
    val sourceType: Column<String> = varchar(SOURCE_TYPE_COLUMN).bindTo { it.sourceType }
    val dimension: Column<Int> = int(DIMENSION_COLUMN).bindTo { it.dimension }
    val createAt: Column<LocalDateTime> = datetime(CREATE_AT_COLUMN).bindTo { it.createdAt }
    val deleteFlag: Column<Boolean> = boolean(DELETE_FLAG).bindTo { it.deleteFlag }
}

interface EmbeddingRecord : Entity<EmbeddingRecord> {

    var id: Long
    var modelName: String
    var config: EmbeddingConfigVal
    var sourceType: String
    var dimension: Int
    var createdAt: LocalDateTime
    var deleteFlag: Boolean
}


val DatabaseHandler.embeddingRecords get() = this.database.sequenceOf(EmbeddingRecords)