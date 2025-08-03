package cn.bobasyu.agentv.domain.service.agent

import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.domain.aggregate.ChatAggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.repository.comand.AgentCommandRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.McpId
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit


class AgentArmoryFactory(
    private val agentCommandRepository: AgentCommandRepository
) {
    private val agentHolder: LoadingCache<AgentId, AgentEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<AgentId, AgentEntity>() {
            override fun load(agentId: AgentId) = agentCommandRepository.query().findAgentEntity(agentId)
        })
    private val chatModelEntityHolder: LoadingCache<ChatModelId, ChatModelEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<ChatModelId, ChatModelEntity>() {
            override fun load(chatModelId: ChatModelId) = agentCommandRepository.query().findChatModelEntity(chatModelId)
        })
    private val mcpEntityHolder: LoadingCache<McpId, McpEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<McpId, McpEntity>() {
            override fun load(mcpId: McpId) = agentCommandRepository.query().findMcpEntity(mcpId)
        })

    fun agentEntity(agentId: AgentId): AgentEntity = agentHolder[agentId]

    fun chatModelEntity(chatModelId: ChatModelId): ChatModelEntity = chatModelEntityHolder[chatModelId]

    fun mcpEntity(mcpId: McpId): McpEntity = mcpEntityHolder[mcpId]

    fun agentAggregate(agentId: AgentId): ChatAggregate {
        val agentEntity = agentEntity(agentId)
        val chatModelEntity = chatModelEntity(agentEntity.chatModelId)
        return ChatAggregate(
            agent = agentEntity,
            chatModel = chatModelEntity,
            messages = mutableListOf()
        )
    }

    fun agentAggregate(chatModelEntity: ChatModelEntity): ChatAggregate {
        val agentId = AgentId(generateId())
        val agentEntity = agentEntity(agentId)
        val chatModelEntity = chatModelEntity(agentEntity.chatModelId)
        return ChatAggregate(
            agent = agentEntity,
            chatModel = chatModelEntity,
            messages = mutableListOf()
        )
    }
}
