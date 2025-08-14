package cn.bobasyu.agentv.domain.vals


/**
 * 工作流节点
 */
interface ChainNode<in REQ, out RESP> {
    fun process(req: REQ): RESP
}

/**
 * 工作流链路
 */
class Chain<RESP>(
    private val producer: () -> RESP
) {
    /**
     *  执行
     */
    fun execute(): RESP = producer()

    /**
     * 操作符重载：实现 类似 | 管道操作符
     */
    infix fun <NEXT_RESP> pipe(next: ChainNode<RESP, NEXT_RESP>): Chain<NEXT_RESP> {
        return Chain {
            next.process(this@Chain.execute())
        }
    }

    /**
     * 操作符重载：实现 + 组合操作符
     */
    operator fun <NEXT_RESP> plus(next: ChainNode<RESP, NEXT_RESP>): Chain<NEXT_RESP> {
        return this pipe next
    }
}