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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.sha.calendar.kotlin.lib.interfaces.DayView
import com.sha.calendar.kotlin.lib.interfaces.IDayData
import com.sha.calendar.kotlin.lib.interfaces.IDayView
import com.sha.calendar.kotlin.lib.interfaces.IDayViewAdapterCallback

/**
 * Created by Shahul Hameed Shaik  on 06,December,2018
 * Email: android.shahul@gmail.com
 */
class DayViewAdapter(private val context: Context) : IDayViewAdapterCallback {

    override fun onCreateDayView(): IDayView {
        val inflater = LayoutInflater.from(this.context)
        val mainView = inflater.inflate(R.layout.sample_custom_day_view, null, false)
        val dayOfMonthTextView = mainView.findViewById<TextView>(R.id.day_value)

        val dayView = DayView()
        dayView.setDayView(mainView)
        dayView.setDateValueTextView(dayOfMonthTextView)

        return dayView
    }

    override fun onUpDateDayView(view: View, dayData: IDayData) {
        val sampleTextView = view.findViewById<TextView>(R.id.sampleText)
        if (sampleTextView != null) {
            sampleTextView.text = "hi hello"
            sampleTextView.setTextColor(view.context.resources.getColor(R.color._light_green))
        }
    }
}

