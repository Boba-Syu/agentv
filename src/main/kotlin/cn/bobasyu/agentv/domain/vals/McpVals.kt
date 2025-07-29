package cn.bobasyu.agentv.domain.vals

enum class McpTransportType {
    SSE, STDIO
}

data class McpConfigVal(
    val command: String,
    val args: List<String>
)

class MCPBuilder {
    lateinit var command: String
    lateinit var args: List<String>

    fun build() = McpConfigVal(command, args)

    fun command(command: String) {
        this.command = command
    }

    fun args(vararg args: String) {
        this.args = args.toList()
    }
}

fun mcp(fn: MCPBuilder.() -> Unit): McpConfigVal = with(MCPBuilder()) {
    fn()
    build()
}
