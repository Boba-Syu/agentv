package cn.bobasyu.agentv.config

import cn.bobasyu.agentv.common.utils.ConfigName

@ConfigName("server")
data class ServerConfig(
    val port: Int
)

@ConfigName("database")
data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
)

@ConfigName("bangumi")
data class BangumiConfig(
    val authorization: String,
    val baseUrl: String,
    val userAgent: String
)

@ConfigName("ollama")
data class OllamaConfig(
    val baseUrl: String
)

@ConfigName("embedding")
data class EmbeddingConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String,
    val createTable: Boolean = true,
)