package cn.bobasyu.agentv.domain.bangumi.repository.query

import cn.bobasyu.agentv.domain.bangumi.entity.BangumiCalendarEntity
import cn.bobasyu.agentv.domain.bangumi.entity.BangumiDetailEntity
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiCalendarWeekdayEnum
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum

interface BangumiRepository {
    /**
     * 新番时刻表
     */
    fun calendar(): Map<BangumiCalendarWeekdayEnum, List<BangumiCalendarEntity>>

    /**
     * 根据关键词查询词条
     */
    fun searchByKeyword(keyword: String, types: List<BangumiSubjectTypeEnum> = listOf()): List<BangumiDetailEntity>
}