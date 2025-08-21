package cn.bobasyu.agentv.domain.base.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.base.vals.FunctionCallExecutor
import cn.bobasyu.agentv.domain.base.vals.FunctionCallParam
import cn.bobasyu.agentv.domain.base.vals.ToolId
import kotlin.reflect.KClass

/**
 * function call 工具实体
 */
class ToolEntity(
    /**
     * 工具id
     */
    override val id: ToolId,
    /**
     * 工具名称
     */
    var name: String,
    /**
     * 工具描述
     */
    var description: String,
    /**
     * 工具参数
     */
    var parameters: MutableList<FunctionCallParam>,
    /**
     * 工具执行器
     */
    var functionCallExecutor: KClass<FunctionCallExecutor>
) : Entity<ToolId>(id)