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

package com.sha.calendar.kotlin.lib.utilites

import android.support.v7.widget.RecyclerView

import java.util.Calendar
import java.util.Date

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
object ShaValidationUtils {

    fun checkDateNull(date: Date?) {
        if (date == null) {
            throw IllegalArgumentException(
                    "From: or To: dates should not be null")
        }
    }

    fun checkCalendarNull(calendar: Calendar?) {
        if (calendar == null) {
            throw IllegalArgumentException(
                    "From: or To: calendar should not be null")
        }
    }

    fun checkFromToDates(from: Date, to: Date) {
        if (!from.isBeforeDay(to)) {
            if (!from.isSameDay(to)) {
                throw IllegalArgumentException(
                        "From: date should be before To: date, please check from, to dates")
            }
        }
    }

    fun checkFromToCalendar(from: Calendar, to: Calendar) {
        if (!from.time.isBeforeDay(to.time)) {
            if (!from.time.isSameDay(to.time)) {
                throw IllegalArgumentException(
                        "From: calendar should be before To: calendar, please check From, To dates ")
            }

        }
    }

    fun checkHighlightDate(date: Date, from: Calendar, to: Calendar) {
        val highlightCal = Calendar.getInstance()
        highlightCal.time = date

        if (date.isSameDay(from.time) || date.isSameDay(to.time)) {
            return
        }

        if (!(date.after(from.time) && date.before(to.time))) {
            throw IllegalArgumentException(
                    "Highlight date is not valid, its not within calendar range. Please check given highlight date")
        }
    }

    fun checkMonthValidation(month: Int) {
        if (!(month >= 1 && month <= 12)) {
            throw IllegalArgumentException("Month value will be allowed from 1 to 12, but the value given: $month")
        }
    }

    fun checkSelectDates(from: Date, to: Date, selectionMode: SelectionMode) {

        if (selectionMode !== SelectionMode.RANGE) {
            throw IllegalArgumentException(
                    "Please set selection mode with RANGE in Calendar Settings")
        }

        if (from.after(to)) {
            throw IllegalArgumentException(
                    "From: date should be before To: date in Range")
        }
    }

    fun checkSelectDateWithinCalendarRange(date: Date, from: Calendar, to: Calendar) {
        if (date.isSameDay(from.time) || date.isSameDay(to.time)) {
            return
        }

        if (!(date.after(from.time) && date.before(to.time))) {
            throw IllegalArgumentException(
                    "Selected date is not valid, its not within calendar range. Please check given select date")
        }
    }

    fun checkInfiniteLoad(iCalendarScrollListener: RecyclerView.OnScrollListener?, isLoadInfinite: Boolean) {
        if (iCalendarScrollListener != null) {
            if (!isLoadInfinite) {
                throw IllegalArgumentException(
                        "please initialize setting setLoadInfinite to be true")
            }
        }
    }
}
