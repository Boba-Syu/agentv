package cn.bobasyu.agentv.common.utils

fun String.camelToSnakeCase(): String {
    return this.replace(Regex("([A-Z])")) { "_${it.groupValues[1].lowercase()}" }
        .removePrefix("_")
        .lowercase()
}