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

package com.sha.calendar.kotlin.lib.interfaces

import android.view.View
import android.widget.TextView

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
interface IDayView {
    var view: View?
    var dayValueTextView: TextView?
    fun setDayView(view: View)
    fun setDateValueTextView(textView: TextView)
}
