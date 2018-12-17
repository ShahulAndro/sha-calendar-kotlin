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
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.sha.calendar.kotlin.lib.interfaces.IDayViewAdapterCallback
import com.sha.calendar.kotlin.lib.model.DayData
import com.sha.calendar.kotlin.lib.utilites.ShaLocaleUtils
import com.sha.calendar.kotlin.lib.viewholder.DayViewHolder

import java.util.ArrayList
import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

 class DayDataAdapter : BaseAdapter, Cloneable {

    private var canDisplayPreviousNextMonthDays: Boolean = false
    private var isRTL: Boolean = false
    private var isForceDisplayRTL: Boolean = false

    var dayDataList: MutableList<DayData> = ArrayList()

    private var dayAdapterCallback: DayAdapterCallback? = null
    private var dayViewAdapterCallback: IDayViewAdapterCallback? = null
    private var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    override fun getCount(): Int {
        return this.dayDataList.size
    }

    override fun getItem(position: Int): Any? {
        return if (position < 0) {
            null
        } else this.dayDataList[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var viewHolder: DayViewHolder? = null
        var convertView = convertView

        if (convertView == null) {
            val dayView = this.dayViewAdapterCallback?.onCreateDayView()
            dayView?.let {
                convertView = dayView!!.view
                convertView?.let { viewHolder = DayViewHolder(convertView!!) }
                dayView!!.dayValueTextView?.let { viewHolder?.setDayOfMonthTextView(dayView!!.dayValueTextView!!)  }
                this.dayAdapterCallback?.let { viewHolder?.setDayAdapterCallback(this.dayAdapterCallback!!) }
                convertView?.tag = viewHolder
            }
        } else {
            viewHolder = convertView?.tag as DayViewHolder
        }

        val dayData = this.dayDataList[position]
        viewHolder?.setDayData(dayData)
        viewHolder?.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays)
        viewHolder?.isRTL(this.isRTL)
        viewHolder?.setForceDisplayRTL(this.isForceDisplayRTL)

        viewHolder?.displayDataInView()

        this.dayViewAdapterCallback?.onUpDateDayView(convertView!!, dayData)
        return convertView!!
    }

    fun setCallback(dayAdapterCallback: DayAdapterCallback) {
        this.dayAdapterCallback = dayAdapterCallback
    }

    fun setDayViewAdapterCallback(dayViewAdapterCallback: IDayViewAdapterCallback) {
        this.dayViewAdapterCallback = dayViewAdapterCallback
    }

    fun isRTL(locale: Locale) {
        this.isRTL = ShaLocaleUtils.isRTL(locale)
    }

    fun setForceDisplayRTL(forceDisplayRTL: Boolean) {
        this.isForceDisplayRTL = forceDisplayRTL
    }

    fun canDisplayPreviousNextMonthDays(): Boolean {
        return canDisplayPreviousNextMonthDays
    }

    fun setDisplayPreviousNextMonthDays(canDisplayPreviousNextMonthDays: Boolean) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays
    }

    inner class DayViewOnClickListener : DayViewOnClickCallback {
        override fun onClick(view: View, dayData: DayData) {
            dayData.isSelected = true
            this@DayDataAdapter.dayAdapterCallback?.onDateClicked(dayData)
        }
    }

    interface DayAdapterCallback {
        open fun onDateClicked(dayData: DayData)
    }

    interface DayViewOnClickCallback {
        open fun onClick(view: View, dayData: DayData)
    }
}
