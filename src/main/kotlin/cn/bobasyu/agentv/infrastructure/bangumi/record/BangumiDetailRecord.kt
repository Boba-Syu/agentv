package cn.bobasyu.agentv.infrastructure.bangumi.record

import cn.bobasyu.agentv.domain.bangumi.entity.BangumiDetailEntity
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiImageVal
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiInfoboxVal
import cn.bobasyu.agentv.domain.bangumi.vals.BangumiTagVal
import cn.bobasyu.agentv.domain.bangumi.vals.bangumiSubjectTypeEnum
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class BangumiDetailRecord(
    val platform: String,

    val date: LocalDate,

    val images: BangumiImageVal,

    val summary: String,

    val name: String,

    val type: Int,

    @JsonProperty("name_cn")
    val nameCn: String,

    val tags: List<BangumiTagVal>,

    @JsonProperty("meta_tags")
    val metaTags: List<String>,

    val infobox: List<BangumiInfoboxVal>,
) {
    fun bangumiDetailEntity() =
        BangumiDetailEntity(
            this.platform,
            this.date,
            this.images,
            this.summary,
            this.name,
            this.nameCn,
            this.tags,
            this.metaTags,
            this.infobox,
            bangumiSubjectTypeEnum(this.type)
        )
}

class BangumiSearchResp(
    val data: List<BangumiDetailRecord>,
    val total: Int,
    val limit: Int,
    val offset: Int
)