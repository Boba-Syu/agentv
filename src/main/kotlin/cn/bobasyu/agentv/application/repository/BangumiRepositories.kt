package cn.bobasyu.agentv.application.repository

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.domain.bangumi.repository.query.BangumiRepository
import cn.bobasyu.agentv.infrastructure.bangumi.query.BangumiRepositoryImpl

object BangumiRepositories {
    object Query {

    }

    object Command {
        val bangumiRepository: BangumiRepository by lazy {
            BangumiRepositoryImpl(ApplicationContext.instance)
        }
    }
}

