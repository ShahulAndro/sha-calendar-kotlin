package com.sha.calendar.kotlin.lib.utilites

import java.util.*


/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

fun Calendar.calendarWith(year: Int, month: Int, day: Int): Calendar {
    ShaValidationUtils.checkMonthValidation(month)

    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, day)
    return cal
}

fun Calendar.calendarWith(year: Int, month: Int): Calendar {
    ShaValidationUtils.checkMonthValidation(month)

    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    return cal
}

fun Calendar.calendarWith(date: Date, timeZone: TimeZone, locale: Locale): Calendar {
    val calendar = Calendar.getInstance(timeZone, locale)
    calendar.time = date
    return calendar
}

fun Calendar.dayOfMonthWith(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Calendar.monthWith(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MONTH)
}

fun Calendar.yearWith(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.YEAR)
}


fun Calendar.monthKey(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    return "$year-$month"
}

fun Calendar.clearHoursMinSecMillsFromCalendar() {
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
}