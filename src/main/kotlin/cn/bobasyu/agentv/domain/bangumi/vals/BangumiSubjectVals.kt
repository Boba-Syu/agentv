package cn.bobasyu.agentv.domain.bangumi.vals

enum class BangumiSubjectTypeEnum(
    val code: Int,

    val description: String,
) {
    BOOK(1, "书籍"),
    ANIMATION(2, "动画"),
    MUSIC(3, "音乐"),
    GAME(4, "游戏"),
    OTHERS(6, "其他")
}

fun bangumiSubjectTypeEnum(code: Int): BangumiSubjectTypeEnum {
    for (value in BangumiSubjectTypeEnum.entries) {
        if (value.code == code) {
            return value
        }
    }

    throw IllegalArgumentException("Invalid code: $code")
}

data class BangumiImageVal(
    val large: String = "",

    val common: String = "",

    val medium: String = "",

    val small: String = "",

    val grid: String = "",
)

data class BangumiTagVal(
    val name: String,
    val count: Long,
    val totalCount: Long,
)

data class BangumiInfoboxVal(
    val key: String,
    val value: Any
)
