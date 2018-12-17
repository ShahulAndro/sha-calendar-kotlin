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

package com.calendar.kotlin.example

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast

import com.sha.calendar.kotlin.lib.interfaces.IDayData
import com.sha.calendar.kotlin.lib.utilites.SelectionMode
import com.sha.calendar.kotlin.lib.utilites.calendarWith
import com.sha.calendar.kotlin.lib.view.CalendarPickerRecyclerView

import java.util.Calendar
import java.util.Locale

class MainActivity : Activity() {

    private var calendarPickerRecyclerView: CalendarPickerRecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.calendarPickerRecyclerView = findViewById(R.id.calendar_recyclerview)
        this.calendarPickerRecyclerView?.layoutManager = LinearLayoutManager(this)
        this.calendarPickerRecyclerView?.
                initSettings()?.
                setDisplayPreviousNextMonthDaysByMonth(false)?.
                setDisplayWeekDaysByMonth(true)?.
                setSelectionMode(SelectionMode.SINGLE)?.
                apply()?.
                loadContent()

        this.calendarPickerRecyclerView?.setCalendarCallback(object : CalendarPickerRecyclerView.CalendarCallback {
            override fun onDateSelected(dayData: IDayData) {
                Toast.makeText(applicationContext, "Date selected:" + dayData.date, Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<View>(R.id.single_selection).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setSelectionMode(SelectionMode.SINGLE)?.
                    apply()?.
                    loadContent()
        }

        findViewById<View>(R.id.multiple_selection).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setSelectionMode(SelectionMode.MULTIPLE)?.
                    apply()?.
                    loadContent()
        }


        findViewById<View>(R.id.range_selection).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setSelectionMode(SelectionMode.RANGE)?.
                    apply()?.
                    loadContent()
        }

        findViewById<View>(R.id.rtl_selection).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setLocale(Locale("ar"))?.
                    setForceDisplayRTL(true)?.
                    setSelectionMode(SelectionMode.RANGE)?.
                    apply()?.
                    loadContent()
        }

        findViewById<View>(R.id.custom_dayview).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setDayViewAdapterCallback(DayViewAdapter(applicationContext))?.
                    apply()?.
                    loadContent()
        }

        findViewById<View>(R.id.highlight).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    apply()?.
                    loadContent()?.
                    highlightDates(Calendar.getInstance().calendarWith(2018, 12, 31).time)
        }

        findViewById<View>(R.id.select_dates).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setSelectionMode(SelectionMode.RANGE)?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    apply()?.
                    loadContent()

            val from1 = Calendar.getInstance().calendarWith(2019, 1, 1)
            val to1 = Calendar.getInstance().calendarWith(2019, 1, 15)
            calendarPickerRecyclerView?.selectDates(from1, to1)

            val from2 = Calendar.getInstance().calendarWith(2019, 2, 15)
            val to2 = Calendar.getInstance().calendarWith(2019, 2, 28)
            calendarPickerRecyclerView?.selectDates(from2, to2)
        }

        findViewById<View>(R.id.infinite_load).setOnClickListener {
            calendarPickerRecyclerView?.
                    initSettings()?.
                    setDisplayPreviousNextMonthDaysByMonth(false)?.
                    setDisplayWeekDaysByMonth(true)?.
                    setLoadInfinite(true)?.
                    apply()?.
                    loadContent()
        }
    }
}
