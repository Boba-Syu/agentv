package cn.bobasyu.agentv.domain.bangumi.vals

enum class BangumiCalendarWeekdayEnum(
    val id: Int,
    val en: String,
    val cn: String,
    val ja: String
) {
    MONDAY(1, "Mon", "星期一", "月曜日"),
    TUESDAY(2, "Tue", "星期二", "火曜日"),
    WEDNESDAY(3, "Wed", "星期三", "水曜日"),
    THURSDAY(4, "Thu", "星期四", "木曜日"),
    FRIDAY(5, "Fri", "星期五", "金曜日"),
    SATURDAY(6, "Sat", "星期六", "土曜日"),
    SUNDAY(7, "Sum", "星期日", "日曜日")
}

fun bangumiCalendarWeekdayEnum(id: Int): BangumiCalendarWeekdayEnum {
    for (weekday in BangumiCalendarWeekdayEnum.entries) {
        if (weekday.id == id) {
            return weekday
        }
    }
    throw IllegalArgumentException("Invalid weekday id: $id")
}