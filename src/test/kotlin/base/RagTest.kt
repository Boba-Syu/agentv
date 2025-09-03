package cn.bobasyu.test.base

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.application.repository.AgentRepositories.Command.ragRepository
import cn.bobasyu.agentv.application.service.AgentServices.Factory.agentArmoryFactory
import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.EmbeddingConfigVal
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.RagId
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.OllamaTextEmbedder
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.OllamaVectorStoreAdapter
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class RagTest {
    fun applicationText(vertx: Vertx): ApplicationContext = ApplicationContext(vertx)

    val embeddingEntity = EmbeddingEntity(
        id = RagId(generateId()),
        modelName = "bge-m3",
        sourceType = ModelSourceType.OLLAMA,
        embeddingSetting = EmbeddingConfigVal(),
        dimension = 1024
    )

    @BeforeEach
    fun before(vertx: Vertx) {
        ApplicationContext.instance = applicationText(vertx)
    }

    @Test
    fun ollamaTextEmbedder(testContext: VertxTestContext) {
        val ollamaTextEmbedder = OllamaTextEmbedder(embeddingEntity)
        val embedText = ollamaTextEmbedder.embedText("小城日常")
        println(embedText)
        println(embedText.size)
        testContext.completeNow()
    }

    @Test
    fun ollamaVectorStoreAdapterTest(testContext: VertxTestContext) {

        val textSegmentVal = TextSegmentVal(
            text = """
                《CITY THE ANIMATION》（中文名：小城日常）是由京都动画（京阿尼）制作的原创动画，于2025年7月6日开始在TOKYO MX电视台播出，每周日更新。这部动画以“平凡日常”为主题，融合了搞笑、治愈与青春元素，展现了居民们编织出的奇妙生活。
                ### 核心亮点：
                - **风格**：  
                  通过幽默的日常片段（如“噗嗤嗤！”“怦怦♡”），呈现轻松又温暖的氛围，带有京阿尼标志性的细腻情感刻画。
                - **制作团队**：  
                  由导演石立太一（代表作《冰菓》《凉宫春日》系列）执导，音乐由知名组合“ピラニアンズ”负责，原作漫画由あらゐけいいち（《冰菓》《凉宫春日》作者）创作。
                - **播出信息**：  
                  共13集，每周日更新，首播时间为2025年7月6日，其他播出平台包括ABCテレビ、テレビ愛知、BS11等。
                
                ### 其他信息：
                - **官网**：[https://city-the-animation.com/](https://city-the-animation.com/)  
                - **标签**：日常、搞笑、治愈、漫改、京都动画、2025夏番  
                - **看点**：  
                  除了轻松的日常故事，动画还可能暗藏“平凡中的不平凡”，适合喜欢温馨剧情与生活化幽默的观众。
            """.trimIndent(),
            metadata = mapOf("field" to "bangumi")
        )
        val ollamaTextEmbedder = OllamaTextEmbedder(embeddingEntity)
        val vector = ollamaTextEmbedder.embedText(textSegmentVal.text)

        val ollamaVectorStoreAdapter = OllamaVectorStoreAdapter(embeddingEntity)
        ollamaVectorStoreAdapter.initStorage()
        ollamaVectorStoreAdapter.storeVectors(listOf(vector), listOf(textSegmentVal))
        testContext.completeNow()
    }

    @Test
    fun langChainAnswerGeneratorTest(testContext: VertxTestContext) {
        val chatModelEntity = agentArmoryFactory.chatModelEntity("qwen3:8b", ModelSourceType.OLLAMA)
        var answerQuestion = ragRepository.answerQuestion("CITY THE ANIMATION 是什么时候播出的, 导演是谁", embeddingEntity, chatModelEntity)
        println(answerQuestion.response)
        println(answerQuestion.supportSegments)
        println("----------------")
        answerQuestion = ragRepository.answerQuestion("小城日常 是什么时候播出的, 导演是谁", embeddingEntity, chatModelEntity)
        println(answerQuestion.response)
        println(answerQuestion.supportSegments)
        testContext.completeNow()
    }
}