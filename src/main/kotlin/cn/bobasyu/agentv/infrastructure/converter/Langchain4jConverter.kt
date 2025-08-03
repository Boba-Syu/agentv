package cn.bobasyu.agentv.infrastructure.converter

import cn.bobasyu.agentv.domain.entity.ChatModelEntity
import cn.bobasyu.agentv.domain.vals.McpConfigVal
import cn.bobasyu.agentv.domain.vals.MessageRole
import cn.bobasyu.agentv.domain.vals.MessageVal
import cn.bobasyu.agentv.domain.vals.SseMcpConfigVal
import cn.bobasyu.agentv.domain.vals.StdioMcpConfigVal
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.mcp.client.DefaultMcpClient
import dev.langchain4j.mcp.client.transport.McpTransport
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport
import dev.langchain4j.model.ollama.OllamaChatModel


fun langChain4jMessages(messageVals: List<MessageVal>) =
    messageVals.map { langChain4jMessage(it) }

fun langChain4jMessage(messageVal: MessageVal) = when (messageVal.role) {
    MessageRole.SYSTEM -> SystemMessage(messageVal.content)
    MessageRole.USER -> UserMessage(messageVal.content)
    MessageRole.ASSISTANT -> AiMessage(messageVal.content)
}

fun ollamaChatModel(chatModelEntity: ChatModelEntity): OllamaChatModel = OllamaChatModel.builder()
    .modelName(chatModelEntity.modelName)
    .temperature(chatModelEntity.config?.temperature)
    .build()


fun mcpClients(mcpConfigVals: List<McpConfigVal>) = mcpConfigVals.map { mcpClient(it) }
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