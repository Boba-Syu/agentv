package cn.bobasyu.agentv.domain.vals

data class EmbeddingConfigVal(
    val maxResult: Int?,
    val minScore: Double?,
    val filter: List<EmbeddingFilterCondition>
)

enum class FilterMatchType {
    EQUAL,
}

data class EmbeddingFilterCondition(
    val key: String,
    val value: Any,
    val embeddingFilterMatch: FilterMatchType
)
