package cn.bobasyu.agentv.domain.vals

data class EmbeddingConfigVal(
    val maxResult: Int?,
    val minScore: Double?,
    val filter: List<EmbeddingFilterCondition>
)

enum class FilterMatchType {
    EQUAL,
}

fun filterMatchType(str: String): FilterMatchType = when (str.lowercase()) {
    "equal" -> FilterMatchType.EQUAL
    else -> throw IllegalArgumentException("Invalid filter match type: $str")
}

data class EmbeddingFilterCondition(
    val key: String,
    val value: Any,
    val embeddingFilterMatch: FilterMatchType
)
