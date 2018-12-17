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

package com.sha.calendar.kotlin.lib.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.calendar.lib.R
import com.sha.calendar.kotlin.lib.interfaces.IDayViewAdapterCallback
import com.sha.calendar.kotlin.lib.model.DayData
import com.sha.calendar.kotlin.lib.model.MonthData
import com.sha.calendar.kotlin.lib.utilites.monthKey
import com.sha.calendar.kotlin.lib.viewholder.MonthRecyclerViewHolder

import java.util.HashMap
import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class MonthRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<MonthRecyclerViewHolder>(), DayDataAdapter.DayAdapterCallback {

    private var isForceDisplayRTL: Boolean = false
    private var locale: Locale? = null
    private var dayViewAdapterCallback: IDayViewAdapterCallback? = null
    private var monthRecyclerViewAdapterCallback: MonthRecyclerViewAdapterCallback? = null
    private var inflater: LayoutInflater? = null
    private var monthDataMap: MutableMap<String, MonthData>? = null

    var monthDataList: List<MonthData>? = null
        set(monthDataList) {
            field = monthDataList
            updatedMonthsDataListWithMap()
        }

    init {
        this.inflater = LayoutInflater.from(context)
        this.monthDataMap = HashMap()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MonthRecyclerViewHolder {
        val itemView = inflater!!.inflate(R.layout.month_days_grid_view, null, false)
        val viewHolder = MonthRecyclerViewHolder(this.context, itemView)
        viewHolder.setCallback(this)
        this.locale?.let { viewHolder.setLocale(this.locale!!) }
        viewHolder.setForceDisplayRTL(this.isForceDisplayRTL)
        this.dayViewAdapterCallback?.let { viewHolder.setDayViewAdapterCallback(this.dayViewAdapterCallback!!) }
        return viewHolder
    }

    override fun onBindViewHolder(monthViewHolder: MonthRecyclerViewHolder, position: Int) {
        val monthData = this.monthDataList!![position]
        monthViewHolder.bindView(monthData)
    }

    override fun getItemCount(): Int {
        return if (this.monthDataList == null || this.monthDataList!!.count() < 1) {
            0
        } else this.monthDataList!!.count()

    }

    override fun onDateClicked(dayData: DayData) {
        this.monthRecyclerViewAdapterCallback?.onDaySelectedInMonth(dayData)
    }

    fun setCallback(monthRecyclerViewAdapterCallback: MonthRecyclerViewAdapterCallback) {
        this.monthRecyclerViewAdapterCallback = monthRecyclerViewAdapterCallback
    }

    fun setDayViewAdapterCallback(dayViewAdapterCallback: IDayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback
    }

    fun setLocale(locale: Locale) {
        this.locale = locale
    }

    fun setForceDisplayRTL(forceDisplayRTL: Boolean) {
        isForceDisplayRTL = forceDisplayRTL
    }

    private fun updatedMonthsDataListWithMap() {
        if (this.monthDataList != null && !this.monthDataList!!.isEmpty()) {
            for (i in 0.. (this.monthDataList!!.count() -1)) {
                val monthData = this.monthDataList!![i]
                monthDataMap?.put(monthData.date!!.monthKey(), monthData)
            }
        }
    }

    interface MonthRecyclerViewAdapterCallback {
        fun onDaySelectedInMonth(dayData: DayData)
    }
}
