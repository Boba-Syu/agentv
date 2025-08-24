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

data class BangumiImageVal(
    val large: String = "",

    val common: String = "",

    val medium: String = "",

    val small: String = "",

    val grid: String = "",
)