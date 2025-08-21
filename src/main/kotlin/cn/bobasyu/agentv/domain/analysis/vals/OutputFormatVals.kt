package cn.bobasyu.agentv.domain.analysis.vals

enum class OutputFormatType(
    val type: String,
    val description: String
) {
    NUMBER("number", "数字"),
    STRING("string", "字符串"),
    TIMESTAMP("timestamp", "时间戳"),
    BOOLEAN("boolean", "布尔值"),
    NUMBER_ARRAY("array", "数字类型数组"),
    STRING_ARRAY("array", "字符串类型数组")
}

annotation class OutputFormat(
    val type: OutputFormatType,
    val description: String
)