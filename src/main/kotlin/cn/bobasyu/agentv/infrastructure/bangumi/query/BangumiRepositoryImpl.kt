package cn.bobasyu.agentv.infrastructure.bangumi.query

import cn.bobasyu.agentv.application.ApplicationContext
import cn.bobasyu.agentv.common.utils.parseJson
import cn.bobasyu.agentv.common.utils.parseJsonToList
import cn.bobasyu.agentv.config.BangumiConfig
import cn.bobasyu.agentv.domain.bangumi.entity.BangumiCalendarEntity
import cn.bobasyu.agentv.domain.bangumi.entity.BangumiDetailEntity
import cn.bobasyu.agentv.domain.bangumi.repository.query.BangumiRepository
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiCalendarId
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiCalendarWeekdayEnum
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import cn.bobasyu.agentv.domain.bangumi.vals.bangumiCalendarWeekdayEnum
import cn.bobasyu.agentv.infrastructure.bangumi.record.BangumiCalendarRecord
import cn.bobasyu.agentv.infrastructure.bangumi.record.BangumiSearchResp
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.util.stream.Collectors

class BangumiRepositoryImpl(
    val applicationContext: ApplicationContext
) : BangumiRepository {

    private val bangumiConfig: BangumiConfig = applicationContext.config[BangumiConfig::class]

    private val headers: Map<String, String>
        get() = mapOf(
            "Authorization" to bangumiConfig.authorization,
            "User-Agent" to bangumiConfig.userAgent
        )

    /**
     * 新番时刻表
     */
    override fun calendar(): Map<BangumiCalendarWeekdayEnum, List<BangumiCalendarEntity>> {
        val url = "${bangumiConfig.baseUrl}/calendar"
        val resp: String = applicationContext.httpClient.get(url = url, params = null, headers = headers)!!
        val calendarList: List<BangumiCalendarRecord> = resp.parseJsonToList(BangumiCalendarRecord::class.java)
        return calendarList.stream()
            .collect(
                Collectors.toMap(
                    { bangumiCalendarWeekdayEnum(it.items[0].airWeekday) },
                    {
                        it.items.map { item ->
                            BangumiCalendarEntity(
                                id = BangumiCalendarId(item.id),
                                url = item.url,
                                type = item.type,
                                name = item.name,
                                nameCn = item.nameCn,
                                summary = item.summary,
                                airWeekday = bangumiCalendarWeekdayEnum(item.airWeekday),
                                airDate = item.airDate,
                                images = item.images,
                            )
                        }
                    })
            )
    }

    /**
     * 根据关键词查询词条
     */
    override fun searchByKeyword(keyword: String, types: List<BangumiSubjectTypeEnum>): List<BangumiDetailEntity> {
        val url = "${bangumiConfig.baseUrl}/v0/search/subjects"
        val params: JsonObject = json {
            if (types.isNotEmpty()) {
                obj("types" to types)
            }
            obj("keyword" to keyword)
        }
        val resp = applicationContext.httpClient.post(url, params, headers)!!
        val bangumiDetailRecordList = resp.parseJson(BangumiSearchResp::class.java).data
        if (types.isEmpty()) {
            return bangumiDetailRecordList.map { it.bangumiDetailEntity() }
        }
        val typeCodes = types.map { it.code }
        return bangumiDetailRecordList.filter { typeCodes.contains(it.type) }
            .map { it.bangumiDetailEntity() }
    }
}