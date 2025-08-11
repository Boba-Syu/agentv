package cn.bobasyu.agentv.infrastructure.converter

import cn.bobasyu.agentv.common.utils.parseJsonToMap
import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.entity.ToolEntity
import cn.bobasyu.agentv.domain.vals.FunctionCallBooleanParam
import cn.bobasyu.agentv.domain.vals.FunctionCallEnumParam
import cn.bobasyu.agentv.domain.vals.FunctionCallExecutor
import cn.bobasyu.agentv.domain.vals.FunctionCallNumberParam
import cn.bobasyu.agentv.domain.vals.FunctionCallParam
import cn.bobasyu.agentv.domain.vals.FunctionCallStringParam
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageRole
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.SseMcpConfigVal
import cn.bobasyu.agentv.domain.vals.StdioMcpConfigVal
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.mcp.client.DefaultMcpClient
import dev.langchain4j.mcp.client.transport.McpTransport
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema
import dev.langchain4j.model.chat.request.json.JsonNumberSchema
import dev.langchain4j.model.chat.request.json.JsonObjectSchema
import dev.langchain4j.model.chat.request.json.JsonSchemaElement
import dev.langchain4j.model.chat.request.json.JsonStringSchema
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.service.tool.ToolExecutor
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun langChain4jMessage(messageVal: MessageVal): ChatMessage = when (messageVal.role) {
    MessageRole.SYSTEM -> SystemMessage(messageVal.content)
    MessageRole.USER -> UserMessage(messageVal.content)
    MessageRole.ASSISTANT -> AiMessage(messageVal.content)
}

fun ollamaChatModel(chatModelEntity: ChatModelEntity): OllamaChatModel = OllamaChatModel.builder()
    .modelName(chatModelEntity.modelName)
    .temperature(chatModelEntity.config?.temperature)
    .build()

fun mcpClients(mcpConfigVals: List<McpConfigVal>): List<DefaultMcpClient> = mcpConfigVals.map { mcpClient(it) }

fun mcpClient(mcpConfigVal: McpConfigVal): DefaultMcpClient = DefaultMcpClient.Builder()
    .transport(mcpTransport(mcpConfigVal))
    .build()

fun mcpTransport(mcpConfigVal: McpConfigVal): McpTransport = when (mcpConfigVal) {
    is StdioMcpConfigVal -> stdioMcpTransport(mcpConfigVal)
    is SseMcpConfigVal -> sseMcpTransport(mcpConfigVal)
}

fun stdioMcpTransport(mcpConfigVal: StdioMcpConfigVal): StdioMcpTransport {
    val command = mutableListOf(mcpConfigVal.command)
    mcpConfigVal.args.forEach { command.add(it) }
    return StdioMcpTransport.Builder()
        .command(command)
        .logEvents(true)
        .build()
}

fun sseMcpTransport(mcpConfigVal: SseMcpConfigVal): HttpMcpTransport = HttpMcpTransport.Builder()
    .sseUrl(mcpConfigVal.url)
    .logRequests(true)
    .logResponses(true)
    .build()

fun toolSpecification(toolEntity: ToolEntity): ToolSpecification =
    ToolSpecification.builder()
        .name(toolEntity.name)
        .description(toolEntity.description)
        .parameters(jsonObjectSchema(toolEntity))
        .build()

fun jsonObjectSchema(toolEntity: ToolEntity): JsonObjectSchema {
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

fun toolExecutor(functionCallExecutor: KClass<FunctionCallExecutor>) = ToolExecutor { toolExecutionRequest, _ ->
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