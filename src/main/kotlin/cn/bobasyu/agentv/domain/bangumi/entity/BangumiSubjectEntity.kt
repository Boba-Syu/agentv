package cn.bobasyu.agentv.domain.bangumi.entity

import cn.bobasyu.agentv.common.vals.Entity
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiImageVal
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectId
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiSubjectTypeEnum
import java.time.LocalDate

data class BangumiSubjectEntity(
    override val id: BangumiSubjectId,

    val type: BangumiSubjectTypeEnum,

    val name: String = "",

    val nameCn: String = "",

    val summary: String = "",

    val date: LocalDate,

    val platform: String = "",

    val images: BangumiImageVal = BangumiImageVal(),

    val infoBox: String = "",

    /**
     * 书籍条目的册数
     */
    val volumes: Int? = null,

    /**
     * 数据库中的章节数量
     */
    val totalEpisodes: Int? = null,
) : Entity<BangumiSubjectId>(id)
