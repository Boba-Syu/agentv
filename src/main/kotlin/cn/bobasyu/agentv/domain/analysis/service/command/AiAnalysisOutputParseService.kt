package cn.bobasyu.agentv.domain.analysis.service.command

import cn.bobasyu.agentv.domain.analysis.vals.OutputFormat
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotations

class AiAnalysisOutputParseService {

    fun parseToPrompt(clazz: KClass<*>): String {
        val properties: Collection<KProperty1<out Any, *>> = clazz.declaredMemberProperties

        return with(StringBuilder()) {
            append("返回结果为一下结构：\n ")
            append("{")
            for (property in properties) {
                val annotation: List<OutputFormat> = property.findAnnotations(OutputFormat::class)
                if (annotation.isNotEmpty()) {
                    append("\"${property.name}\": \"${annotation[0].type.description}, ${annotation[0].description}\"\n")
                }
            }
            append("}")
            toString()
        }
    }
}