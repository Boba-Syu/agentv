package cn.bobasyu.agentv.infrastructure.repository.command.rag

import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.vals.MetadataFilter
import cn.bobasyu.agentv.domain.vals.TextSegmentVal


/**
 * 向量存储适配器：抽象向量数据库操作
 */
interface VectorStoreAdaptor {

    /**
     * 初始化向量存储
     */
    fun initStorage(embeddingEntity: EmbeddingEntity, dimensions: Int)

    /**
     * 存储向量和对应文本片段
     * @param vectors 向量列表
     * @param segments 文本片段列表
     */
    fun storeVectors(embeddingEntity: EmbeddingEntity, vectors: List<List<Float>>, segments: List<TextSegmentVal>)

    /**
     * 相似性搜索
     * @param queryVector 查询向量
     * @param maxResults 最大返回结果数
     * @return 最相关的文本片段
     */
    fun similaritySearch(
        embeddingEntity: EmbeddingEntity,
        queryVector: MutableList<Float>,
        maxResults: Int
    ): List<TextSegmentVal>

    /**
     * 带过滤条件的相似性搜索
     * @param queryVector 查询向量
     * @param filter 元数据过滤器
     * @param maxResults 最大返回结果数
     * @return 过滤后的相关片段
     */
    fun similaritySearchWithFilter(
        embeddingEntity: EmbeddingEntity,
        queryVector: List<Float>,
        filter: List<MetadataFilter>,
        maxResults: Int
    ): List<TextSegmentVal>
}