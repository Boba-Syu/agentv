package cn.bobasyu.agentv.domain.bangumi.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiCalendarId
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiCalendarWeekdayEnum
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiImageVal
import java.time.LocalDate

data class BangumiCalendarEntity(
    override val id: BangumiCalendarId,
    val url: String,
    val type: Int,
    val name: String,
    val nameCn: String,
    val summary: String,
    /**
     * 放送星期
     */
    val airWeekday: BangumiCalendarWeekdayEnum,
    /**
     * 放送开始日期
     */
    val airDate: LocalDate,
    val images: BangumiImageVal
) : Entity<BangumiCalendarId>(id)