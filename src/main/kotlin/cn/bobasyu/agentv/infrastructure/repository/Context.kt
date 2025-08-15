package cn.bobasyu.agentv.infrastructure.repository

import cn.bobasyu.agentv.application.repository.Command
import cn.bobasyu.agentv.infrastructure.repository.command.message.PersistentChatMemoryStore

object RepositoryContext {
    val persistentChatMemoryStore by lazy { PersistentChatMemoryStore(Command.agentRepository) }
}

