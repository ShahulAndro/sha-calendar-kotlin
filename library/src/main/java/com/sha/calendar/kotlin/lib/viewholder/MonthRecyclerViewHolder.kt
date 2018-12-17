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

package com.sha.calendar.kotlin.lib.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView

import com.calendar.lib.R
import com.sha.calendar.kotlin.lib.adapter.DayDataAdapter
import com.sha.calendar.kotlin.lib.interfaces.IDayViewAdapterCallback
import com.sha.calendar.kotlin.lib.model.MonthData
import com.sha.calendar.kotlin.lib.utilites.ShaLocaleUtils
import com.sha.calendar.kotlin.lib.utilites.WeekdayNames

import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class MonthRecyclerViewHolder : RecyclerView.ViewHolder {

    private var isForceDisplayRTL: Boolean = false

    private var weekdayNames: WeekdayNames? = null
    private var monthData: MonthData? = null
    private var callback: DayDataAdapter.DayAdapterCallback? = null
    private var dayViewAdapterCallback: IDayViewAdapterCallback? = null

    private var locale: Locale? = null
    private var view: View? = null
    private var context: Context? = null

    constructor(itemView: View) : super(itemView) {
        this.view = itemView
    }

    constructor(context: Context, itemView: View) : super(itemView) {
        this.context = context
        this.view = itemView
    }

    fun setCallback(dayAdapterCallback: DayDataAdapter.DayAdapterCallback) {
        this.callback = dayAdapterCallback
    }

    fun setLocale(locale: Locale) {
        this.locale = locale
    }

    fun setForceDisplayRTL(forceDisplayRTL: Boolean) {
        this.isForceDisplayRTL = forceDisplayRTL
    }

    fun setDayViewAdapterCallback(dayViewAdapterCallback: IDayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback
    }

    fun bindView(monthData: MonthData) {
        applyRTLOnViews()

        this.monthData = monthData

        val textView = this.view?.findViewById<TextView>(R.id.title)
        textView?.text = this.monthData?.label

        displayWeekDays()

        val gridView = this.view?.findViewById<GridView>(R.id.month_days_gridview)
        this.context?.let {
            val dayDataAdapter = DayDataAdapter(it)
            this.monthData?.let { dayDataAdapter.dayDataList = this.monthData!!.getDayDataList() }
            this.callback?.let { dayDataAdapter.setCallback(this.callback!!) }
            this.dayViewAdapterCallback.let { dayDataAdapter.setDayViewAdapterCallback(this.dayViewAdapterCallback!!) }
            this.locale?.let { dayDataAdapter.isRTL(locale!!) }
            dayDataAdapter.setForceDisplayRTL(this.isForceDisplayRTL)
            dayDataAdapter.setDisplayPreviousNextMonthDays(monthData.canDisplayPreviousNextMonthDays())
            gridView?.adapter = dayDataAdapter
        }
    }

    private fun displayWeekDays() {
        val weekDaysView = this.view?.findViewById<LinearLayout>(R.id.week_days)
        this.monthData?.let {
            if (!it.canDisplayWeekDays()) {
                weekDaysView?.visibility = View.GONE
                return
            }
        }

        this.locale?.let { this.weekdayNames = WeekdayNames(it) }

        setDayName(R.id.dayName1, 1)
        setDayName(R.id.dayName2, 2)
        setDayName(R.id.dayName3, 3)
        setDayName(R.id.dayName4, 4)
        setDayName(R.id.dayName5, 5)
        setDayName(R.id.dayName6, 6)
        setDayName(R.id.dayName7, 7)
    }

    private fun setDayName(dayTextId: Int, day: Int) {
        val shortWeekDayName = this.view?.findViewById<TextView>(dayTextId)
        if (shortWeekDayName != null) {
            this.weekdayNames?.let {
                shortWeekDayName.text = it.shortNameWeekdays[day]
            }
        }
    }

    private fun applyRTLOnViews() {
        if (!ShaLocaleUtils.isRTL(Locale.getDefault()) && this.isForceDisplayRTL) {
            applyRTLOnMonthTextView()
            applyRTLOnWeekDays()
            applyRTLOnGridViews()
        }
    }

    private fun applyRTLOnMonthTextView() {
        this.view?.findViewById<View>(R.id.title)?.rotationY = ShaLocaleUtils.rotateYForView().toFloat()
    }

    private fun applyRTLOnWeekDays() {
        val viewGroup = this.view?.findViewById<ViewGroup>(R.id.week_days)
        viewGroup?.let {
            for (i in 0..(viewGroup.childCount - 1)) {
                viewGroup.getChildAt(i).rotationY = ShaLocaleUtils.rotateYForView().toFloat()
            }
        }
    }

    private fun applyRTLOnGridViews() {
        this.view?.rotationY = ShaLocaleUtils.rotateYForViewGroup().toFloat()
    }
}
