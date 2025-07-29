package cn.bobasyu.agentv.common.vals

abstract class Id(open val id: Long)

abstract class Entity<T : Id> (open val id: Id)

abstract class Aggregate<T : Id>  (open val id: Id)