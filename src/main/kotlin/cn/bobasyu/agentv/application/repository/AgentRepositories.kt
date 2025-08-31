package cn.bobasyu.agentv.application.repository

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.domain.base.repository.comand.AgentRepository
import cn.bobasyu.agentv.domain.base.repository.comand.RagRepository
import cn.bobasyu.agentv.domain.base.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.infrastructure.base.repository.command.AgentRepositoryImpl
import cn.bobasyu.agentv.infrastructure.base.repository.command.RagRepositoryImpl
import cn.bobasyu.agentv.infrastructure.base.repository.query.AgentQueryRepositoryImpl

object AgentRepositories {

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
        val ragRepository: RagRepository by lazy {
            RagRepositoryImpl()
        }
    }
}
