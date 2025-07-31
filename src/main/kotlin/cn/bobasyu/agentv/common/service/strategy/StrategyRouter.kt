package cn.bobasyu.agentv.common.service.strategy

/**
 * 策略处理器
 */
interface StrategyHandler<REQ, CONTEXT, RESP> {

    fun apply(req: REQ, context: CONTEXT): RESP?
}

/**
 * 策略映射器
 */
interface StrategyMapper<REQ, CONTEXT, RESP> {

    fun getHandler(): StrategyHandler<REQ, CONTEXT, RESP>? = endNode()
}

/**
 * 策略结束节点
 */
fun <REQ, CONTEXT, RESP> endNode() = object : StrategyHandler<REQ, CONTEXT, RESP> {
    override fun apply(req: REQ, context: CONTEXT): RESP? {
        return null
    }
}

/**
 * 策略路由抽象类
 */
abstract class AbstractStrategyRouter<REQ, CONTEXT, RESP>
    : StrategyHandler<REQ, CONTEXT, RESP>, StrategyMapper<REQ, CONTEXT, RESP> {

    fun route(req: REQ, context: CONTEXT): RESP? {
        return (getHandler() ?: endNode()).apply(req, context)
    }

}
