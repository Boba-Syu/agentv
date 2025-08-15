package cn.bobasyu.agentv.domain.service.agent

import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.domain.aggregate.AgentAggregate
import cn.bobasyu.agentv.domain.entity.AgentEntity
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.McpEntity
import cn.bobasyu.agentv.domain.repository.query.AgentQueryRepository
import cn.bobasyu.agentv.domain.vals.AgentId
import cn.bobasyu.agentv.domain.vals.ChatModelConfigVal
import cn.bobasyu.agentv.domain.vals.ChatModelId
import cn.bobasyu.agentv.domain.vals.ModelSourceType
import cn.bobasyu.agentv.domain.vals.McpId
import cn.bobasyu.agentv.domain.vals.SystemMessageVal
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit


class AgentArmoryFactory(
    private val agentQueryRepository: AgentQueryRepository
) {
    private val agentHolder: LoadingCache<AgentId, AgentEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<AgentId, AgentEntity>() {
            override fun load(agentId: AgentId) = agentQueryRepository.findAgentEntity(agentId)
        })
    private val chatModelEntityHolder: LoadingCache<ChatModelId, ChatModelEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<ChatModelId, ChatModelEntity>() {
            override fun load(chatModelId: ChatModelId) = agentQueryRepository.findChatModelEntity(chatModelId)
        })
    private val mcpEntityHolder: LoadingCache<McpId, McpEntity> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<McpId, McpEntity>() {
            override fun load(mcpId: McpId) = agentQueryRepository.findMcpEntity(mcpId)
        })

    fun agentEntity(agentId: AgentId): AgentEntity = agentHolder[agentId]

    fun chatModelEntity(chatModelId: ChatModelId): ChatModelEntity = chatModelEntityHolder[chatModelId]

    fun chatModelEntity(
        modelName: String,
        sourceType: ModelSourceType,
        role: SystemMessageVal? = null,
        config: ChatModelConfigVal? = null
    ): ChatModelEntity {
        val chatModelEntity = ChatModelEntity(
            id = ChatModelId(generateId()),
            modelName = modelName,
            sourceType = sourceType,
            role = role,
            config = config
        )
        chatModelEntityHolder.put(chatModelEntity.id, chatModelEntity)
        return chatModelEntity
    }

    fun mcpEntity(mcpId: McpId): McpEntity = mcpEntityHolder[mcpId]

    fun chatAggregate(agentId: AgentId): AgentAggregate {
        val agentEntity = agentEntity(agentId)
        return AgentAggregate(
            agent = agentEntity,
            messages = mutableListOf()
        )
    }

    fun chatAggregate(chatModelEntity: ChatModelEntity): AgentAggregate {
        val agentId = AgentId(generateId())
        val agentEntity = AgentEntity(id = agentId, chatModelId = chatModelEntity.id)
        val agentAggregate = AgentAggregate(
            agent = agentEntity,
            messages = agentQueryRepository.findMessages(agentId).toMutableList()
        )
        agentHolder.put(agentId, agentEntity)
        return agentAggregate
    }

    fun chatAggregate(agentEntity: AgentEntity): AgentAggregate {
        return AgentAggregate(
            agent = agentEntity,
            messages = agentQueryRepository.findMessages(agentEntity.id).toMutableList()
        )
    }
}
