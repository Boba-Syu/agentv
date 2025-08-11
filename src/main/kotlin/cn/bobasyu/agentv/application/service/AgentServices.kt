package cn.bobasyu.agentv.application.service

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.application.repository.Query
import cn.bobasyu.agentv.domain.service.agent.AgentArmoryFactory

object Factory {
    private val agentArmoryFactory by lazy {
        AgentArmoryFactory(
            agentQueryRepository = Query.agentQueryRepository,
        )
    }
}