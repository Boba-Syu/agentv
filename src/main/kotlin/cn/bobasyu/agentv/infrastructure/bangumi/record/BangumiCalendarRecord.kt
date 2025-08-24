package cn.bobasyu.agentv.infrastructure.bangumi.record

import cn.bobasyu.agentv.domain.bangumi.vals.BangumiImageVal
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDate

data class BangumiCalendarRecord(
    val items: List<BangumiCalendarItemRecord>
) : Serializable


data class BangumiCalendarItemRecord(
    val id: Long,
    val url: String,
    val type: Int,
    val name: String,
    @JsonProperty("name_cn")
    val nameCn: String,
    val summary: String,
    /**
     * 放送星期
     */
    @JsonProperty("air_weekday")
    val airWeekday: Int,
    /**
     * 放送开始日期
     */
    @JsonProperty("air_date")
    val airDate: LocalDate,
    val images: BangumiImageVal
) : Serializable
