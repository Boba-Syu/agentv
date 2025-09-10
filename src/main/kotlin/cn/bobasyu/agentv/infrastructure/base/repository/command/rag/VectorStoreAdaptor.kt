package cn.bobasyu.agentv.infrastructure.base.repository.command.rag

import cn.bobasyu.agentv.domain.base.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.base.vals.MetadataFilter
import cn.bobasyu.agentv.domain.base.vals.ModelSourceType
import cn.bobasyu.agentv.domain.base.vals.TextSegmentVal
import cn.bobasyu.agentv.infrastructure.base.repository.command.rag.impl.PgVectorStoreAdapter
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit


/**
 * 向量存储适配器：抽象向量数据库操作
 */
interface VectorStoreAdaptor {

    /**
     * 初始化向量存储
     */
    fun initStorage()

    /**
     * 存储向量和对应文本片段
     * @param vectors 向量列表
     * @param segments 文本片段列表
     */
    fun storeVectors(vectors: List<List<Float>>, segments: List<TextSegmentVal>)

    /**
     * 相似性搜索
     * @param queryVector 查询向量
     * @param maxResults 最大返回结果数
     * @param filter 元数据过滤器
     * @return 过滤后的相关片段
     */
    fun similaritySearch(
        queryVector: List<Float>,
        maxResults: Int,
        filter: List<MetadataFilter> = listOf()
    ): List<TextSegmentVal>
}


object VectorStoreAdaptorFactory {

    private val vectorStoreAdaptor: LoadingCache<EmbeddingEntity, VectorStoreAdaptor> = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(object : CacheLoader<EmbeddingEntity, VectorStoreAdaptor>() {
            override fun load(embeddingEntity: EmbeddingEntity) = when (embeddingEntity.sourceType) {
                ModelSourceType.OPENAI -> TODO()
                ModelSourceType.VOLCENGINE -> TODO()
                ModelSourceType.OLLAMA -> PgVectorStoreAdapter(embeddingEntity)
            }
        })

    fun textEmbedder(embeddingEntity: EmbeddingEntity): VectorStoreAdaptor = vectorStoreAdaptor.get(embeddingEntity)
}