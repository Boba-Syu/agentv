package cn.bobasyu.agentv.domain.vals

/**
 * 函数调用参数
 */
sealed class FunctionCallParam(
    /**
     * 参数名称
     */
    open val name: String,
    /**
     * 参数描述
     */
    open val description: String,
)

/**
 * 函数调用参数, 字符串类型
 */
data class FunctionCallStringParam(
    /**
     * 参数名称
     */
    override val name: String,
    /**
     * 参数描述
     */
    override val description: String,
) : FunctionCallParam(name, description)

/**
 * 函数调用参数, 数字类型
 */
data class FunctionCallNumberParam(
    /**
     * 参数名称
     */
    override val name: String,
    /**
     * 参数描述
     */
    override val description: String,
) : FunctionCallParam(name, description)

/**
 * 函数调用参数, 布尔类型
 */
data class FunctionCallBooleanParam(
    /**
     * 参数名称
     */
    override val name: String,
    /**
     * 参数描述
     */
    override val description: String,
) : FunctionCallParam(name, description)

/**
 * 函数调用参数, 枚举类型
 */
data class FunctionCallEnumParam(
    /**
     * 参数名称
     */
    override val name: String,
    /**
     * 参数描述
     */
    override val description: String,
    /**
     * 参数枚举
     */
    val enums: List<String>
) : FunctionCallParam(name, description)

/**
 * 函数调用执行器
 */
interface FunctionCallExecutor {
    /**
     * 执行函数调用
     */
    fun execute(args: Map<String, Any>): String
}