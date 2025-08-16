package cn.bobasyu.agentv.infrastructure.converter

import cn.bobasyu.agentv.common.utils.parseJsonToMap
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.EmbeddingEntity
import cn.bobasyu.agentv.domain.entity.ToolEntity
import cn.bobasyu.agentv.domain.vals.FunctionCallBooleanParam
import cn.bobasyu.agentv.domain.vals.FunctionCallEnumParam
import cn.bobasyu.agentv.domain.vals.FunctionCallExecutor
import cn.bobasyu.agentv.domain.vals.FunctionCallNumberParam
import cn.bobasyu.agentv.domain.vals.FunctionCallStringParam
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageRole
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.SseMcpConfigVal
import cn.bobasyu.agentv.domain.vals.StdioMcpConfigVal
import cn.bobasyu.agentv.domain.vals.TextSegmentVal
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.mcp.client.DefaultMcpClient
import dev.langchain4j.mcp.client.transport.McpTransport
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport
import dev.langchain4j.model.chat.request.json.JsonObjectSchema
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.model.ollama.OllamaEmbeddingModel
import dev.langchain4j.service.tool.ToolExecutor
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun toLangChain4jMessage(messageVal: MessageVal): ChatMessage = when (messageVal.role) {
    MessageRole.SYSTEM -> SystemMessage(messageVal.content)
    MessageRole.USER -> UserMessage(messageVal.content)
    MessageRole.ASSISTANT -> AiMessage(messageVal.content)
}

fun toOllamaChatModel(chatModelEntity: ChatModelEntity): OllamaChatModel = OllamaChatModel.builder()
    .modelName(chatModelEntity.modelName)
    .temperature(chatModelEntity.config?.temperature)
    .build()

fun toMcpClients(mcpConfigVals: List<McpConfigVal>): List<DefaultMcpClient> = mcpConfigVals.map { toMcpClient(it) }

fun toMcpClient(mcpConfigVal: McpConfigVal): DefaultMcpClient = DefaultMcpClient.Builder()
    .transport(toMcpTransport(mcpConfigVal))
    .build()

fun toMcpTransport(mcpConfigVal: McpConfigVal): McpTransport = when (mcpConfigVal) {
    is StdioMcpConfigVal -> toStdioMcpTransport(mcpConfigVal)
    is SseMcpConfigVal -> toSseMcpTransport(mcpConfigVal)
}

fun toStdioMcpTransport(mcpConfigVal: StdioMcpConfigVal): StdioMcpTransport {
    val command = mutableListOf(mcpConfigVal.command)
    mcpConfigVal.args.forEach { command.add(it) }
    return StdioMcpTransport.Builder()
        .command(command)
        .logEvents(true)
        .build()
}

fun toSseMcpTransport(mcpConfigVal: SseMcpConfigVal): HttpMcpTransport = HttpMcpTransport.Builder()
    .sseUrl(mcpConfigVal.url)
    .logRequests(true)
    .logResponses(true)
    .build()

fun toToolSpecification(toolEntity: ToolEntity): ToolSpecification =
    ToolSpecification.builder()
        .name(toolEntity.name)
        .description(toolEntity.description)
        .parameters(toJsonObjectSchema(toolEntity))
        .build()

fun toJsonObjectSchema(toolEntity: ToolEntity): JsonObjectSchema {
    val jsonObjectSchema = JsonObjectSchema.builder()
    toolEntity.parameters.forEach { param ->
        when (param) {
            is FunctionCallStringParam -> jsonObjectSchema.addStringProperty(param.name, param.description)
            is FunctionCallBooleanParam -> jsonObjectSchema.addBooleanProperty(param.name, param.description)
            is FunctionCallEnumParam -> jsonObjectSchema.addEnumProperty(param.name, param.enums, param.description)
            is FunctionCallNumberParam -> jsonObjectSchema.addNumberProperty(param.name, param.description)
        }
    }
    return jsonObjectSchema.build()
}

fun toToolExecutor(functionCallExecutor: KClass<FunctionCallExecutor>) = ToolExecutor { toolExecutionRequest, _ ->
    try {
        val functionCallExecutor = functionCallExecutor.createInstance()
        val arguments: String = toolExecutionRequest.arguments()
        val args = arguments.parseJsonToMap()
        functionCallExecutor.execute(args)
    } catch (e: Exception) {
        // 日志记录或包装异常返回错误信息
        throw RuntimeException("Tool execution failed: ${e.message}", e)
    }
}

fun toOllamaEmbeddingModel(embeddingEntity: EmbeddingEntity): OllamaEmbeddingModel {
    val embeddingModel: OllamaEmbeddingModel = OllamaEmbeddingModel.builder()
        .modelName(embeddingEntity.modelName)
        .build()
    return embeddingModel
}

fun toTextSegment(domainSegment: TextSegmentVal): TextSegment {
    val metadata = dev.langchain4j.data.document.Metadata(domainSegment.metadata)
    return TextSegment.from(domainSegment.text, metadata)
}

fun toTextSegmentVal(langChainSegment: TextSegment): TextSegmentVal =
    TextSegmentVal(
        text = langChainSegment.text(),
        metadata = langChainSegment.metadata().toMap() ?: mutableMapOf(),
    )
