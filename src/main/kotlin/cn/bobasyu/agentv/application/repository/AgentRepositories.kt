package cn.bobasyu.agentv.application.repository

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.infrastructure.repository.command.AgentRepositoryImpl
import cn.bobasyu.agentv.infrastructure.repository.command.RagRepositoryImpl
import cn.bobasyu.agentv.infrastructure.repository.query.AgentQueryRepositoryImpl

object Query {
    val agentQueryRepository: cn.bobasyu.agentv.domain.base.repository.query.AgentQueryRepository by lazy {
        AgentQueryRepositoryImpl(ApplicationContext.instance.databaseHandler)
    }
}

object Command {
    val agentRepository: cn.bobasyu.agentv.domain.base.repository.comand.AgentRepository by lazy {
        AgentRepositoryImpl(
            Query.agentQueryRepository,
            ApplicationContext.instance.databaseHandler
        )
    }
    val ragRepository: cn.bobasyu.agentv.domain.base.repository.comand.RagRepository by lazy {
        RagRepositoryImpl()
    }
}