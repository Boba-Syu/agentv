package cn.bobasyu.agentv.common.repository

interface BaseQueryRepository

interface BaseCommandRepository<T : BaseQueryRepository> {
    fun query(): T
}