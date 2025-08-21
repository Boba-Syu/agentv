package cn.bobasyu.agentv.domain.base.vals

/**
 * 文档实体
 */
data class TextSegmentVal(
    /**
     * 片段文本内容
     */
    val text: String,
    /**
     * 元数据
     */
    val metadata: Map<String, Any>,
)

/**
 * 答案实体
 */
data class AnswerVal(
    /**
     * 生成的最终答案
     */
    val response: String,
    /**
     * 支撑答案的片段
     */
    val supportSegments: List<TextSegmentVal>,

    /**
     * 处理时间
     */
    val processTime: Long
)

enum class FilterOperator {
    EQUAL, NOT_EQUAL, EXIST
}

/**
 * 原数组过滤器
 */
data class MetadataFilter(
    /**
     * 字段
     */
    val field: String,
    /**
     * 期望值
     */
    val value: Any,

    /**
     * 操作符
     */
    val operator: FilterOperator
)