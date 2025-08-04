package cn.bobasyu.agentv.domain.vals

data class EmbeddingConfigVal(
    val maxResult: Int?,
    val minScore: Double?,
    val filter: List<EmbeddingFilterCondition>
)

enum class FilterMatchType {
    EQUAL,
}

fun filterMatchType(str: String): FilterMatchType {
    for (value in FilterMatchType.entries) {
        if (value.name == str.uppercase()) {
            return value
        }
    }
    throw IllegalArgumentException("Invalid FilterMatchType: $str")
}

data class EmbeddingFilterCondition(
    val key: String,
    val value: Any,
    val embeddingFilterMatch: FilterMatchType
)
