package cn.bobasyu.agentv.application.service

import cn.bobasyu.agentv.application.repository.AgentRepositories.Query.agentQueryRepository
import cn.bobasyu.agentv.domain.base.service.agent.AgentArmoryFactory

object AgentServices  {
    object Factory {
        val agentArmoryFactory by lazy { AgentArmoryFactory(agentQueryRepository) }
    }
}
