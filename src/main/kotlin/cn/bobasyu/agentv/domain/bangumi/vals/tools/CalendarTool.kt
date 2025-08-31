package cn.bobasyu.agentv.domain.bangumi.vals.tools

import cn.bobasyu.agentv.application.repository.BangumiRepositories.Command.bangumiRepository
import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.common.utils.toJson
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.FunctionCallExecutor
import cn.bobasyu.agentv.domain.base.vals.ToolId
import io.vertx.core.impl.logging.LoggerFactory

fun calendarToolEntity(): ToolEntity {
    return ToolEntity(
        id = ToolId(generateId()),
        name = "bangumi_calendar",
        description = "获取当前季度新番日历",
        parameters = mutableListOf(),
        functionCallExecutor = CalendarTool::class
    )
}

class CalendarTool : FunctionCallExecutor {

    private val logger = LoggerFactory.getLogger(FunctionCallExecutor::class.java)

    override fun execute(args: Map<String, Any>): String {
        logger.info("CalendarTool execute")
        val resp = bangumiRepository.calendar().toJson()
        logger.info("CalendarTool execute result: $resp")
        return resp
    }
}