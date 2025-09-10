package cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.common.utils.parseJson
import cn.bobasyu.agentv.config.ViliconflowConfig
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.slf4j.LoggerFactory
import java.io.IOException


class VolcengineScoringModel {
    private val baseUrl: String = "https://api.siliconflow.cn/v1/rerank"
    private val modelName = "BAAI/bge-reranker-v2-m3"

    private val httpClient = ApplicationContext.instance.httpClient

    private val viliconflowConfig = ApplicationContext.instance.config[ViliconflowConfig::class]

    private val log = LoggerFactory.getLogger(VolcengineScoringModel::class.java)

    fun score(text: String, query: String): ScoringResult {
        return calculateRelevanceScore(query, listOf(text)).first()
    }

    fun scoreAll(
        segments: List<TextSegmentVal>,
        query: String
    ): List<ScoringResult> {
        val documents = segments.map { it.text }
        return calculateRelevanceScore(query, documents)
    }

    private fun calculateRelevanceScore(query: String, documents: List<String>): List<ScoringResult> {
        try {
            val params: JsonObject = json {
                obj(
                    "model" to modelName,
                    "query" to query,
                    "documents" to documents
                )
            }
            val header = mapOf(
                "Content-Type" to "application/json",
                "Authorization" to "Bearer ${viliconflowConfig.apiKey}"
            )
            val res = httpClient.post(baseUrl, params, header)!!
            val scoringResponse = res.parseJson(ScoringResponse::class.java)
            return scoringResponse.results
        } catch (e: IOException) {
            log.error("Error calculating relevance score: $e")
        }

        // 降级策略
        return IntRange(0, documents.size - 1).map {
            ScoringResult(
                ScoringDocument(documents[it]),
                it,
                0.5
            )
        }
    }


    data class ScoringResponse(
        val id: String,

        val results: List<ScoringResult>
    )

    data class ScoringResult(
        val document: ScoringDocument?,

        val index: Int,

        @JsonProperty("relevance_score")
        val relevanceScore: Double,
    )

    data class ScoringDocument(
        val text: String?
    )
}