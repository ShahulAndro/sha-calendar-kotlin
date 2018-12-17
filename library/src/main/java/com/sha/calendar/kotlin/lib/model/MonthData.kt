/*
 * Copyright 2018 Shahul Hameed Shaik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sha.calendar.kotlin.lib.model

import com.sha.calendar.kotlin.lib.utilites.dayOfMonthWith
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone

import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class MonthData {
    private var canDisplayWeekDays = true
    private var canDisplayPreviousNextMonthDays = false
    private var isDayDataUpdated: Boolean = false
    private var month: Int = 0
    private var year: Int = 0
    var date: Date ? = null
    private var dayDataList: MutableList<DayData> = ArrayList()
    private val dayDataMap: MutableMap<Int, DayData> = HashMap()

    var label: String? = null
        internal set

    private val previousMonthsDayData: MutableList<DayData>
        get() {
            val previousDayDataList = ArrayList<DayData>()

            val currentMonth = Calendar.getInstance()
            currentMonth.time = this.date
            currentMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK)

            val previousMonth = Calendar.getInstance()
            previousMonth.time = this.date
            previousMonth.add(Calendar.MONTH, -1)
            val maximumDayOfMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (i in 0..(firstDayOfWeek - 2)) {
                val dayData = DayData()
                val calendar = Calendar.getInstance()
                calendar.time = previousMonth.time
                val previousMonthStartDay = maximumDayOfMonth - (firstDayOfWeek - 2) + i
                calendar.set(DAY_OF_MONTH, previousMonthStartDay)
                dayData.date = calendar.time
                dayData.value = calendar.get(DAY_OF_MONTH).toString()
                dayData.isCurrentMonth = false
                previousDayDataList.add(dayData)
            }

            this.isDayDataUpdated = true
            return previousDayDataList
        }

    private val nextMonthsDayData: MutableList<DayData>
        get() {
            val totalWeekDays = 7
            val nextMonthDays = ArrayList<DayData>()

            val currentMonth = Calendar.getInstance()
            currentMonth.time = this.date
            currentMonth.set(DAY_OF_MONTH, currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
            val lastDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK)

            val nextMonth = Calendar.getInstance()
            nextMonth.time = this.date
            nextMonth.add(Calendar.MONTH, 1)
            for (i in 0 .. (totalWeekDays - lastDayOfWeek -1)) {
                val dayData = DayData()
                val calendar = Calendar.getInstance()
                calendar.time = nextMonth.time
                calendar.set(DAY_OF_MONTH, i + 1)
                dayData.date = calendar.time
                dayData.isToday = sameDate(calendar, Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()))
                dayData.value = calendar.get(DAY_OF_MONTH).toString()
                dayData.isCurrentMonth = false
                nextMonthDays.add(dayData)
            }

            this.isDayDataUpdated = true
            return nextMonthDays
        }

    constructor(month: Int, year: Int, date: Date, label: String, dataList: MutableList<DayData>) {
        this.month = month
        this.year = year
        this.date = date
        this.label = label
        this.dayDataList = dataList
        updateDayDataMap(this.dayDataList)
    }

    fun getDayDataList(): MutableList<DayData> {
        if (canDisplayPreviousNextMonthDays) {
            adjustDayDataListWithPreviousNextMonthsDays()
        } else {
            adjustDayDataListWithEmptyWeekDays()
        }

        return dayDataList
    }

    fun setDayDataList(dayDataList: MutableList<DayData>) {
        this.dayDataList = dayDataList
        updateDayDataMap(this.dayDataList)
    }

    fun getDayDataMap(): Map<Int, DayData> {
        return dayDataMap
    }

    fun updateDayDataMap(dayDataList: MutableList<DayData>) {
        for (dayData in dayDataList) {
            if (dayData.isCurrentMonth) {
                this.dayDataMap.put(dayData.date.dayOfMonthWith(), dayData)
            }
        }
    }

    fun canDisplayWeekDays(): Boolean {
        return canDisplayWeekDays
    }

    fun setDisplayWeekDays(canDisplayWeekDays: Boolean) {
        this.canDisplayWeekDays = canDisplayWeekDays
    }

    fun canDisplayPreviousNextMonthDays(): Boolean {
        return canDisplayPreviousNextMonthDays
    }

    fun setDisplayPreviousNextMonthDays(canDisplayPreviousNextMonthDays: Boolean) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays
    }

    private fun adjustDayDataListWithEmptyWeekDays() {
        if (canDisplayPreviousNextMonthDays) {
            return
        }

        if (isDayDataUpdated) {
            return
        }

        val calendar = Calendar.getInstance()
        calendar.time = this.date
        calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        for (i in 0..(firstDayOfWeek - 1 -1)) {
            this.dayDataList.add(i, DayData())
        }

        this.isDayDataUpdated = true
    }

    private fun adjustDayDataListWithPreviousNextMonthsDays() {
        if (!canDisplayPreviousNextMonthDays) {
            return
        }

        if (isDayDataUpdated) {
            return
        }

        this.dayDataList.addAll(0, previousMonthsDayData)
        this.dayDataList.addAll(nextMonthsDayData)
    }

    private fun sameDate(cal: Calendar, selectedDate: Calendar): Boolean {
        return (cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH))
    }

}
