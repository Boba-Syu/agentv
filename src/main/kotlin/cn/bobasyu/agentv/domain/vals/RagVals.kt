package cn.bobasyu.agentv.domain.vals

/**
 * 文档实体
 */
data class DocumentVal(
    /**
     * 文档原始内容
     */
    val content: String,
    /**
     * 元数据键值对（作者、类别、版本等）
     */
    val metadata: Map<String, String>
)

/**
 * 文本片段
 */
data class TextSegmentVal(
    /**
     * 片段文本内容
     */
    val text: String,
    /**
     * 元数据（可继承文档元数据）
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
    val supportSegments: List<TextSegmentVal>
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