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
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.calendar.lib.R
import com.sha.calendar.kotlin.lib.adapter.DayDataAdapter
import com.sha.calendar.kotlin.lib.model.DayData
import com.sha.calendar.kotlin.lib.utilites.RangeState
import com.sha.calendar.kotlin.lib.utilites.ShaLocaleUtils

import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class DayViewHolder(view: View) : View.OnClickListener {

    private var canDisplayPreviousNextMonthDays: Boolean = false
    private var isRTL: Boolean = false
    private var isForceDisplayRTL: Boolean = false

    private var dayData: DayData? = null
    private var dayAdapterCallback: DayDataAdapter.DayAdapterCallback? = null
    private var dayViewOnClickListener: DayDataAdapter.DayViewOnClickListener? = null

    private var dayDataTextView: TextView? = null
    private var rootView: ViewGroup? = null
    private var context: Context? = null

    init {
        this.context = view.context
        this.rootView = view as ViewGroup
        this.dayDataTextView = view.findViewById(R.id.day_value)
        view.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        this.dayData?.let { this.dayAdapterCallback?.onDateClicked(it) }
    }

    fun displayDataInView() {
        applyRTLOnViews()

        this.dayData?.let {
            if (!this.dayData!!.isCurrentMonth) {
                if (this.canDisplayPreviousNextMonthDays) {
                    this.rootView?.isEnabled = false
                    this.rootView?.let {
                        for (i in 0.. (it.childCount - 1)) {
                            this.rootView?.getChildAt(i)?.isEnabled = false
                        }
                    }
                } else {
                    this.rootView?.visibility = View.INVISIBLE
                }
            }
        }

        this.dayDataTextView?.text = this.dayData?.value

        this.dayData?.let {

            if (it.isToday) {
                this.context?.resources?.let {
                    this.rootView?.setBackgroundColor(it.getColor(R.color.sky_blue))
                    this.dayDataTextView?.setTextColor(it.getColor(R.color.white))
                }
            }

            if (it.isHighlighted) {
                this.context?.resources?.let {
                    this.rootView?.setBackgroundColor(it.getColor(R.color.highlighted_day_background))
                    this.dayDataTextView?.setTextColor(it.getColor(R.color.day_text_highlighted))
                }
            }

            if (it.isSelected) {
                if (it.rangeState === RangeState.MIDDLE) {
                    this.context?.resources?.let {
                        this.rootView?.setBackgroundColor(it.getColor(R.color.light_blue5))
                        this.dayDataTextView?.setTextColor(it.getColor(R.color.white))
                    }
                } else {
                    this.context?.resources?.let {
                        this.rootView?.setBackgroundColor(it.getColor(R.color.blue))
                        this.dayDataTextView?.setTextColor(it.getColor(R.color.white))
                    }
                }
            }
        }
    }

    fun setDayAdapterCallback(dayAdapterCallback: DayDataAdapter.DayAdapterCallback) {
        this.dayAdapterCallback = dayAdapterCallback
    }

    fun setDayViewOnClickListener(dayViewOnClickListener: DayDataAdapter.DayViewOnClickListener) {
        this.dayViewOnClickListener = dayViewOnClickListener
    }

    fun setDayData(dayData: DayData) {
        this.dayData = dayData
    }

    fun setDayOfMonthTextView(textView: TextView) {
        this.dayDataTextView = textView
    }

    fun setDisplayPreviousNextMonthDays(canDisplayPreviousNextMonthDays: Boolean) {
        this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays
    }

    fun isRTL(isRTL: Boolean) {
        this.isRTL = isRTL
    }

    fun setForceDisplayRTL(forceDisplayRTL: Boolean) {
        isForceDisplayRTL = forceDisplayRTL
    }

    private fun applyRTLOnViews() {
        if (!ShaLocaleUtils.isRTL(Locale.getDefault()) && this.isForceDisplayRTL) {
            this.rootView?.let {
                for (i in 0..(it.childCount - 1)) {
                    this.rootView?.getChildAt(i)?.rotationY = ShaLocaleUtils.rotateYForView().toFloat()
                }
            }
        }
    }
}

