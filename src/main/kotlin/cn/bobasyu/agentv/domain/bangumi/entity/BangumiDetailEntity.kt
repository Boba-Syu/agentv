package cn.bobasyu.agentv.domain.bangumi.entity

import cn.bobasyu.agentv.domain.bangumi.vals.BangumiImageVal
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiInfoboxVal
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiTagVal
import java.time.LocalDate

data class BangumiDetailEntity(
    val platform: String,
    val date: LocalDate,
    val images: BangumiImageVal,
    val summary: String,
    val name: String,
    val nameCn: String,
    val tags: List<BangumiTagVal>,
    val metaTags: List<String>,
    val infobox: List<BangumiInfoboxVal>,
    val type: BangumiSubjectTypeEnum
)