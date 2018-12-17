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

import java.text.DateFormatSymbols
import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class WeekdayNames {
    private var locale: Locale = Locale.getDefault()

    val fullNameWeekdays: Array<String>
        get() {
            val dateFormatSymbols = DateFormatSymbols(Locale.getDefault())
            return dateFormatSymbols.weekdays
        }

    val shortNameWeekdays: Array<String>
        get() {
            val dateFormatSymbols = DateFormatSymbols(this.locale)
            return dateFormatSymbols.shortWeekdays
        }

    constructor(locale: Locale) {
        this.locale = locale
    }
}
