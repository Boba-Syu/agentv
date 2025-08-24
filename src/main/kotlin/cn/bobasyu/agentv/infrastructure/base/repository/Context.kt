package cn.bobasyu.agentv.infrastructure.base.repository

import cn.bobasyu.agentv.application.repository.Command
import cn.bobasyu.agentv.infrastructure.base.repository.command.message.PersistentChatMemoryStore

object RepositoryContext {
    val persistentChatMemoryStore by lazy { PersistentChatMemoryStore(Command.agentRepository) }
}

