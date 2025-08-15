package cn.bobasyu.agentv.infrastructure.repository.command.rag

import cn.bobasyu.agentv.domain.vals.MetadataFilter
import dev.langchain4j.data.segment.TextSegment


/**
 * 检索器接口：封装上下文检索逻辑
 */
internal interface ContextRetriever {
    /**
     * 检索与问题相关的上下文片段
     * @param question 用户问题
     * @param maxResults 最大返回片段数
     * @return 相关文本片段
     */
    fun retrieveContext(question: String, maxResults: Int): MutableList<TextSegment>

    /**
     * 带元数据过滤的上下文检索
     * @param question 用户问题
     * @param filter 元数据过滤器
     * @param maxResults 最大返回片段数
     * @return 过滤后的相关片段
     */
    fun retrieveContextWithFilter(question: String, filter: MetadataFilter, maxResults: Int): MutableList<TextSegment>
}
