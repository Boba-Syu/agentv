package cn.bobasyu.agentv.domain.analysis.vals

import kotlin.reflect.KClass

data class ChainNodeConfigVal(
    /**
     * 需要执行的任务描述
     */
    val task: String,
    /**
     * 输出格式
     */
    val outputParse: KClass<BaseOutputVal>,
)
