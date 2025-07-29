package cn.bobasyu.agentv.common.http

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import java.io.Closeable

object HttpClientConstant {
    const val AUTHORIZATION = "Authorization"
    const val USER_AGENT = "User-Agent"
}

/**
 * HTTP客户端封装
 */
class HttpClient(
    val vertx: Vertx,
) : Closeable {
    companion object {
        private val log = LoggerFactory.getLogger(HttpClient::class.java)
    }

    private val client by lazy {
        OkHttpClient.Builder().build()
    }

    /**
     * POST请求
     */
    fun post(url: String, params: Map<String, Any>, headers: Map<String, String>): String? {
        val paramJson = JsonObject()
        params.forEach {
            paramJson.put(it.key, it.value)
        }
        return post(url, paramJson, headers)
    }

    /**
     * POST请求
     */
    fun post(url: String, params: JsonObject, headers: Map<String, String>): String? {
        val request = buildPostRequest(url, headers, params)
        return getResponse(request)
    }

    /**
     * GET请求
     */
    fun get(url: String, params: JsonObject?, headers: Map<String, String>): String? {
        val request = buildGetRequest(url, headers, params)
        return getResponse(request)
    }

    /**
     * 发送请求并获取响应
     */
    private fun getResponse(request: Request): String? {
        val call: Call = client.newCall(request)
        call.execute().use { response ->
            response.body.use { body ->
                return@getResponse body?.string()
            }
        }
    }

    /**
     * 构建POST请求对象
     */
    private fun buildPostRequest(url: String, headers: Map<String, String>, params: JsonObject): Request {
        log.info("HTTP POST: url={}, params={}", url, params)
        return with(Request.Builder().url(url)) {
            headers.entries.forEach { (k, v) -> addHeader(k, v) }
            post(params.toString().toRequestBody())
            build()
        }
    }

    /**
     * 构建GET请求对象
     */
    private fun buildGetRequest(url: String, headers: Map<String, String>, params: JsonObject?): Request {
        val urlBuilder = StringBuilder()
        urlBuilder.append(url)

        if (params != null && !params.isEmpty) {
            urlBuilder.append(url).append("?")
            params.forEach { (k, v) ->
                urlBuilder.append("$k=$v")
                urlBuilder.append("&")
            }
        }
        val finalUrl = urlBuilder.removeSuffix("&").toString()
        log.info("HTTP GET: url={}, params={}", finalUrl, params)

        return with(Request.Builder().url(finalUrl)) {
            headers.entries.forEach { (k, v) -> addHeader(k, v) }
            get()
            build()
        }
    }

    override fun close() {}
}