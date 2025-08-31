package cn.bobasyu.test.bangumi;

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.application.repository.BangumiRepositories.Command.bangumiRepository
import cn.bobasyu.agentv.application.service.AgentServices.Factory.agentArmoryFactory
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import cn.bobasyu.agentv.domain.bangumi.vals.tools.bangumiDetailToolEntity
import cn.bobasyu.agentv.domain.bangumi.vals.tools.calendarToolEntity
import cn.bobasyu.agentv.domain.base.vals.ChatModelId
import cn.bobasyu.agentv.domain.base.vals.UserMessageVal
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class BangumiTest {

    fun applicationText(vertx: Vertx): ApplicationContext = ApplicationContext(vertx)

    @BeforeEach
    fun before(vertx: Vertx) {
        ApplicationContext.instance = applicationText(vertx)
    }

    @Test
    fun searchTest(vertx: Vertx, testContext: VertxTestContext) {
        val resp = bangumiRepository.searchByKeyword(keyword = "CITY THE ANIMATION", listOf(BangumiSubjectTypeEnum.ANIMATION))
        println(resp)
        testContext.completeNow()
    }

    @Test
    fun calender(vertx: Vertx, testContext: VertxTestContext) {
        val resp = bangumiRepository.calendar()
        println(resp)
        testContext.completeNow()
    }

    @Test
    fun chatTest(vertx: Vertx, testContext: VertxTestContext) {
        val chatModelEntity = agentArmoryFactory.chatModelEntity(ChatModelId(617272010720391168))
        val chatAggregate = agentArmoryFactory.chatAggregate(chatModelEntity)
        chatAggregate.tools.add(calendarToolEntity())
        chatAggregate.tools.add(bangumiDetailToolEntity())

        var resp = chatAggregate.chat(UserMessageVal("你好, 你能为我做什么"))
        println(resp)
        resp = chatAggregate.chat(UserMessageVal("介绍一下动画 CITY THE ANIMATION"))
        println(resp)
        testContext.completeNow()
    }
}
