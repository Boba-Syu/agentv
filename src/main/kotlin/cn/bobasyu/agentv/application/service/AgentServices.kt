package cn.bobasyu.agentv.application.service

import cn.bobasyu.agentv.application.repository.Query
import cn.bobasyu.agentv.domain.base.service.agent.AgentArmoryFactory

object Factory {
    private val agentArmoryFactory by lazy {
        AgentArmoryFactory(
            agentQueryRepository = Query.agentQueryRepository,
        )
    }
}