package cn.bobasyu.agentv.domain.service.agent

import cn.bobasyu.agentv.domain.aggregate.ChatAggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.repository.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.McpId
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit


class AgentArmoryFactory {
    val agentHolder: LoadingCache<AgentId, AgentEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<AgentId, AgentEntity>() {
            override fun load(agentId: AgentId) = AgentQueryRepository.INSTANCE.findAgentEntity(agentId)
        })
    val chatModelEntityHolder: LoadingCache<ChatModelId, ChatModelEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<ChatModelId, ChatModelEntity>() {
            override fun load(chatModelId: ChatModelId) = AgentQueryRepository.INSTANCE.findChatModelEntity(chatModelId)
        })
    val mcpEntityHolder: LoadingCache<McpId, McpEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<McpId, McpEntity>() {
            override fun load(mcpId: McpId) = AgentQueryRepository.INSTANCE.findMcpEntity(mcpId)
        })

    fun agentEntity(agentId: AgentId): AgentEntity = agentHolder.get(agentId)

    fun chatModelEntity(chatModelId: ChatModelId): ChatModelEntity = chatModelEntityHolder.get(chatModelId)

    fun mcpEntity(mcpId: McpId): McpEntity = mcpEntityHolder.get(mcpId)

    fun agentAggregate(agentId: AgentId): ChatAggregate {
        val agentEntity = agentEntity(agentId)
        val chatModelEntity = chatModelEntity(agentEntity.chatModelId)
        return ChatAggregate(
            agent = agentEntity,
            chatModel = chatModelEntity,
            messages = mutableListOf()
        )
    }
}
