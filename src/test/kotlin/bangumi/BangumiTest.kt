package cn.bobasyu.test.bangumi;

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.common.utils.parseJson
import cn.bobasyu.agentv.common.utils.toJson
import cn.bobasyu.agentv.domain.bangumi.entity.BangumiSubjectEntity
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectId
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import cn.bobasyu.agentv.infrastructure.bangumi.query.BangumiRepositoryImpl
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(VertxExtension::class)
class BangumiTest {

    fun applicationText(vertx: Vertx): ApplicationContext = ApplicationContext(vertx)

    @Test
    fun searchTest(vertx: Vertx, testContext: VertxTestContext) {
        val bangumiRepository = BangumiRepositoryImpl(applicationText(vertx))
        val resp = bangumiRepository.searchByKeyword(keyword = "86")
        println(resp)
        testContext.completeNow()
    }

    @Test
    fun calender(vertx: Vertx, testContext: VertxTestContext) {
        val bangumiRepository = BangumiRepositoryImpl(applicationText(vertx))
        val resp = bangumiRepository.calendar()
        println(resp)
        testContext.completeNow()
    }

    @Test
    fun jsonTest() {
        val bangumiSubject = BangumiSubjectEntity(
            id= BangumiSubjectId(0),
            type = BangumiSubjectTypeEnum.ANIMATION,
            date = LocalDate.now(),
        )
        print(bangumiSubject.toJson())
    }

    @Test
    fun jsonTest2() {
        val bangumiSubject =BangumiSubjectEntity(
            id= BangumiSubjectId(0),
            type = BangumiSubjectTypeEnum.ANIMATION,
            date = LocalDate.now(),
        )
        val json = """
            {"id":0,"type":2,"name":"","name_cn":"","summary":"","date":"2024-12-05","platform":"","images":{"large":"","common":"","medium":"","small":"","grid":""},"infoBox":"","volumes":null,"total_episodes":null}
        """.trimIndent()
        assertEquals(bangumiSubject, json.parseJson(BangumiSubjectEntity::class.java))
    }
}
