package cn.bobasyu.test.base;

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.agentRepository
import cn.bobasyu.agentv.application.service.AgentServices.Factory.agentArmoryFactory
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class AgentTest {

    fun applicationText(vertx: Vertx): ApplicationContext = ApplicationContext(vertx)

    @BeforeEach
    fun before(vertx: Vertx) {
        ApplicationContext.instance = applicationText(vertx)
    }

    @Test
    fun chatTest(vertx: Vertx, testContext: VertxTestContext) {
        val chatModelEntity = agentArmoryFactory.chatModelEntity("qwen3:8b", ModelSourceType.OLLAMA)
        agentRepository.saveModel(chatModelEntity)
        testContext.completeNow()
    }
}
