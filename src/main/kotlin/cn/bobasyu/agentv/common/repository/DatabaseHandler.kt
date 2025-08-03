package cn.bobasyu.agentv.common.repository

import cn.bobasyu.agentv.config.DatabaseConfig
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.BaseTable
import org.ktorm.support.postgresql.InsertOrUpdateStatementBuilder
import org.ktorm.support.postgresql.insertOrUpdate


class DatabaseHandler(
    private val databaseConfig: DatabaseConfig
) {
    private val url get() = databaseConfig.url
    private val user get() = databaseConfig.user
    private val password get() = databaseConfig.password

    val database: Database = Database.connect(url, user = user, password = password)

    fun <T : BaseTable<*>> from(table: T) = database.from(table)

    fun <T : BaseTable<*>> insertOrUpdate(table: T, block: InsertOrUpdateStatementBuilder.(T) -> Unit) = database.insertOrUpdate(table, block)

    fun <T : BaseTable<*>> update(table: T, block: UpdateStatementBuilder.(T) -> Unit) = database.update(table, block)

    fun <T : BaseTable<*>> insert(table: T, block: AssignmentsBuilder.(T) -> Unit) = database.insert(table, block)
}