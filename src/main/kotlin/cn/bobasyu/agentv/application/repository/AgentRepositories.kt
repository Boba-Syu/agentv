package cn.bobasyu.agentv.application.repository

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.domain.repository.comand.AgentRepository
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.infrastructure.repository.command.AgentRepositoryImpl
import cn.bobasyu.agentv.infrastructure.repository.query.AgentQueryRepositoryImpl

object Query {
    val agentQueryRepository: AgentQueryRepository by lazy {
        AgentQueryRepositoryImpl(ApplicationContext.instance.databaseHandler)
    }
}

object Command {
    val agentRepository: AgentRepository by lazy {
        AgentRepositoryImpl(
            Query.agentQueryRepository,
            ApplicationContext.instance.databaseHandler
        )
    }
}