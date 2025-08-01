package cn.bobasyu.agentv.common.utils

import cn.bobasyu.agentv.common.http.HttpResult
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import java.text.SimpleDateFormat


val objectMapper by lazy {
    val mapper = jacksonObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    mapper.registerModule(JavaTimeModule())
    mapper
}

/**
 * 任意类型转Json字符串方法
 */
fun Any.toJson(): String = objectMapper.writeValueAsString(this)

/**
 * json字符串转换为指定类型
 */
fun <T> String.parseJson(objectType: Class<T>): T = objectMapper.readValue(this, objectType)

fun <T, U> String.parseJson(outerType: Class<T>, innerType: Class<U>): T =
    objectMapper.readValue(this, objectMapper.typeFactory.constructParametricType(outerType, innerType))

fun <T> String.parseJsonToHttpResult(type: Class<T>): HttpResult<T> =
    objectMapper.readValue(this, objectMapper.typeFactory.constructParametricType(HttpResult::class.java, type))

fun <T> String.parseJsonToList(type: Class<T>): List<T> =
    objectMapper.readValue(this, objectMapper.typeFactory.constructParametricType(List::class.java, type))

fun String.parseJsonToMap(): Map<String, Any> =
    objectMapper.readValue(this, objectMapper.typeFactory.constructRawMapType(Map::class.java))


/**
 * json序列化器类，在eventBus注册中使用
 */
class BaseCodec<T : Any>(private val type: Class<T>) : MessageCodec<T, T> {

    override fun encodeToWire(buffer: Buffer, t: T) {
        buffer.appendString(t.toJson())
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): T =
        buffer.getString(pos, buffer.length()).parseJson(type)

    override fun transform(t: T): T = t

    override fun name(): String = type.name

    override fun systemCodecID(): Byte = -1
}