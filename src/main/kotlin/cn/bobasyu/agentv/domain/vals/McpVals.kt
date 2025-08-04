package cn.bobasyu.agentv.domain.vals

enum class McpTransportType {
    SSE, STDIO
}

fun mcpTransportType(str: String) = when (str.lowercase()) {
    "sse" -> McpTransportType.SSE
    "stdio" -> McpTransportType.STDIO
    else -> throw IllegalArgumentException("Invalid McpTransportType: $str")
}

sealed interface McpConfigVal

data class StdioMcpConfigVal(
    val command: String,
    val args: List<String>
) : McpConfigVal

data class SseMcpConfigVal(
    val url: String
) : McpConfigVal

class StdioMcpBuilder {
    lateinit var command: String
    lateinit var args: List<String>

    fun build() = StdioMcpConfigVal(command, args)

    fun command(command: String) {
        this.command = command
    }

    fun args(vararg args: String) {
        this.args = args.toList()
    }
}

class SseMcpBuilder {
    lateinit var url: String

    fun build() = SseMcpConfigVal(url)

    fun url(url: String) {
        this.url = url
    }
}

object MCP {

    fun stdio(fn: StdioMcpBuilder.() -> Unit): McpConfigVal = with(StdioMcpBuilder()) {
        fn()
        build()
    }

    fun sse(fn: SseMcpBuilder.() -> Unit): McpConfigVal = with(SseMcpBuilder()) {
        fn()
        build()
    }
}
