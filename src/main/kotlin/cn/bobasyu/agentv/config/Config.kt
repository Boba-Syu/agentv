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