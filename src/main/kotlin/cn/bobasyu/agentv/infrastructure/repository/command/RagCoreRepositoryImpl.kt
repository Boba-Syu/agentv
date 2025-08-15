package cn.bobasyu.agentv.infrastructure.repository.command

import cn.bobasyu.agentv.domain.repository.comand.RagCoreRepository
import cn.bobasyu.agentv.domain.vals.AnswerVal
import cn.bobasyu.agentv.domain.vals.MetadataFilter

class RagCoreRepositoryImpl : RagCoreRepository {

    override fun answerQuestion(question: String): AnswerVal {
        TODO("Not yet implemented")
    }

    override fun answerQuestionWithFilter(
        question: String,
        filter: MetadataFilter
    ): AnswerVal {
        TODO("Not yet implemented")
    }

    override fun answerWithCustomPrompt(
        systemPrompt: String,
        question: String
    ): AnswerVal {
        TODO("Not yet implemented")
    }
}