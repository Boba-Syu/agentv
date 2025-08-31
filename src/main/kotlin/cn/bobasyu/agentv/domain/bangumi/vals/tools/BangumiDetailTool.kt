package cn.bobasyu.agentv.domain.bangumi.vals.tools

import cn.bobasyu.agentv.application.repository.BangumiRepositories.Command.bangumiRepository
import cn.bobasyu.agentv.common.utils.generateId
import cn.bobasyu.agentv.common.utils.toJson
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import cn.bobasyu.agentv.domain.base.entity.ToolEntity
import cn.bobasyu.agentv.domain.base.vals.FunctionCallExecutor
import cn.bobasyu.agentv.domain.base.vals.FunctionCallParam
import cn.bobasyu.agentv.domain.base.vals.FunctionCallStringParam
import cn.bobasyu.agentv.domain.base.vals.ToolId
import io.vertx.core.impl.logging.LoggerFactory

fun bangumiDetailToolEntity(): ToolEntity {
    val parameters = mutableListOf<FunctionCallParam>(
        FunctionCallStringParam("keyword", "番剧名称关键字")
    )

    return ToolEntity(
        id = ToolId(generateId()),
        name = "bangumi_calendar_detail",
        description = "获取番剧详情信息",
        parameters = parameters,
        functionCallExecutor = BangumiDetailTool::class
    )
}

class BangumiDetailTool : FunctionCallExecutor {

    private val logger = LoggerFactory.getLogger(FunctionCallExecutor::class.java)

    override fun execute(args: Map<String, Any>): String {
        val keyword = args["keyword"] as String
        logger.info("调用工具bangumi_calendar_detail, 查询番剧详情, keyword=$keyword")

        logger.info("CalendarTool execute")
        val resp = bangumiRepository.searchByKeyword(
            keyword,
            listOf(BangumiSubjectTypeEnum.ANIMATION)
        ).toJson()
        logger.info("CalendarTool execute result: $resp")
        return resp
    }
}