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

import android.support.v4.text.TextUtilsCompat

import java.util.Locale

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

object ShaLocaleUtils {

    fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.getDisplayName(locale)[0]).toInt()

        return (TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt()
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt())
    }

    fun rotateYForViewGroup(): Int {
        return 180
    }

    fun rotateYForView(): Int {
        return 180
    }
}
