package cn.bobasyu.agentv.config

import cn.bobasyu.agentv.common.http.HttpClient
import cn.bobasyu.agentv.common.utils.ApplicationConfig
import cn.bobasyu.agentv.common.auth.JwtAuth
import cn.bobasyu.agentv.common.repository.DatabaseHandler
import io.vertx.core.Vertx
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import java.io.Closeable

class ApplicationContext(
    private val vertx: Vertx,
    private val configPath: String = "application.yaml",
): Closeable {

    /**
     * 全局配置值
     */
    val config: ApplicationConfig by lazy { ApplicationConfig(configPath) }

    /**
     * 路由
     */
    val router: Router by lazy {
        Router.router(vertx).apply {
            // 设置session
            val store: LocalSessionStore = LocalSessionStore.create(vertx)
            val sessionHandler: SessionHandler = SessionHandler.create(store)
                .setSessionTimeout(24 * 60 * 60 * 100)
            route().handler(sessionHandler)
        }
    }

    /**
     * 数据库链接
     */
    val databaseHandler by lazy { DatabaseHandler(config[DatabaseConfig::class]) }

    /**
     * Jwt鉴权
     */
    val jwtAuth: JwtAuth by lazy { JwtAuth(vertx) }
    val provider: JWTAuth by lazy { jwtAuth.provider }

    /**
     * HTTP客户端封装
     */
    val httpClient: HttpClient by lazy { HttpClient(vertx) }


    override fun close() {

    }

}