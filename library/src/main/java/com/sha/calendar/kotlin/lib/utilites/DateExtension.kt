package com.sha.calendar.kotlin.lib.utilites

import java.util.*


/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
fun Date.isSameDay( date: Date?): Boolean {
    if (date == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = date
    return isSameDay(cal1, cal2)
}

private fun Date.isSameDay(date1: Date?, date2: Date?): Boolean {
    if (date1 == null || date2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = date1
    val cal2 = Calendar.getInstance()
    cal2.time = date2
    return isSameDay(cal1, cal2)
}

private fun Date.isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
    if (cal1 == null || cal2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.isToday(date: Date): Boolean {
    return isSameDay(date, Calendar.getInstance().time)
}

fun Date.isToday(cal: Calendar): Boolean {
    return isSameDay(cal, Calendar.getInstance())
}

fun Date.isBeforeDay(date: Date?): Boolean {
    if (date == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = date
    return isBeforeDay(cal1, cal2)
}

private fun Date.isBeforeDay(date1: Date?, date2: Date?): Boolean {
    if (date1 == null || date2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = date1
    val cal2 = Calendar.getInstance()
    cal2.time = date2
    return isBeforeDay(cal1, cal2)
}

private fun Date.isBeforeDay(cal1: Calendar?, cal2: Calendar?): Boolean {
    if (cal1 == null || cal2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true
    if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false
    if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true
    return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) false else cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.isAfterDay(date: Date?): Boolean {
    if (date == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = date
    return isAfterDay(cal1, cal2)
}

private fun Date.isAfterDay(date1: Date?, date2: Date?): Boolean {
    if (date1 == null || date2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    val cal1 = Calendar.getInstance()
    cal1.time = date1
    val cal2 = Calendar.getInstance()
    cal2.time = date2
    return isAfterDay(cal1, cal2)
}

private fun Date.isAfterDay(cal1: Calendar?, cal2: Calendar?): Boolean {
    if (cal1 == null || cal2 == null) {
        throw IllegalArgumentException("The dates must not be null")
    }
    if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false
    if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true
    if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false
    return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) true else cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.clearTime(date: Date?): Date? {
    if (date == null) {
        return null
    }
    val c = Calendar.getInstance()
    c.time = date
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return c.time
}

fun Date.clearHoursMinSecMillsFromCalendar(cal: Calendar) {
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
}

fun Date.sameDate(calendar1: Calendar, calendar2: Calendar): Boolean {
    return (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
            && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
            && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH))
}

fun Date.dayOfMonthWith(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.isLastDayOfMonth(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
}

fun Date.monthKey(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    return "$year-$month"
}