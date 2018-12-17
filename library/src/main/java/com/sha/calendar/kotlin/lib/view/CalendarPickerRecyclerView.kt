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

package com.sha.calendar.kotlin.lib.view

import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log

import com.calendar.lib.R
import com.sha.calendar.kotlin.lib.adapter.DefaultDayDataView
import com.sha.calendar.kotlin.lib.adapter.MonthRecyclerViewAdapter
import com.sha.calendar.kotlin.lib.interfaces.IDayData
import com.sha.calendar.kotlin.lib.interfaces.IDayViewAdapterCallback
import com.sha.calendar.kotlin.lib.model.DateRange
import com.sha.calendar.kotlin.lib.model.DayData
import com.sha.calendar.kotlin.lib.model.MonthData
import com.sha.calendar.kotlin.lib.utilites.*
import com.sha.calendar.kotlin.lib.viewlistener.RecyclerViewEndlessScrollListener

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.ArrayList
import java.util.Calendar
import java.util.Calendar.*
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */

class CalendarPickerRecyclerView : RecyclerView, MonthRecyclerViewAdapter.MonthRecyclerViewAdapterCallback {

    private var canDisplayWeekDays = true
    private var canDisplayPreviousNextMonthDays = false
    private var canDisplayBorders = false
    private var isForceDisplayRTL = false
    private var isLoadInfinite = false
    private var selectionMode = SelectionMode.SINGLE
    private val defaultLoadMonthsWithInfiniteLoad = 6

    private var fromCal: Calendar? = null
    private var toCal: Calendar? = null
    private var monthCounter: Calendar? = null

    private var locale = Locale.getDefault()
    private var timeZone = TimeZone.getDefault()

    private val monthDataList = ArrayList<MonthData>()
    private val selectedDates = ArrayList<DayData>()
    private val selectedDateRanges = ArrayList<DateRange>()

    private val monthDataMap = HashMap<String, MonthData>()

    private var fromDayData: DayData? = null
    private var toDayData: DayData? = null
    private var dayViewAdapterCallback: IDayViewAdapterCallback? = null
    private var adapter: MonthRecyclerViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var mContext: Context? = null

    private var customScrollListener: RecyclerView.OnScrollListener? = null
    private val calendarSettings: CalendarSettings? = null
    private var calendarCallback: CalendarCallback? = null

    val selectedDays: List<DayData>
        get() = this.selectedDates

    constructor(context: Context) : super(context) {
        this.mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        this.mContext = context
        init()
    }

    override fun getAdapter(): MonthRecyclerViewAdapter? {
        return this.adapter
    }


    private fun reset() {
        monthDataList.clear()
        selectedDates.clear()
        selectedDateRanges.clear()
        monthDataMap.clear()

        removeAllViewsInLayout()
    }

    override fun onDaySelectedInMonth(dayData: DayData) {
        Log.d("Selected date", "" + dayData.date)

        if (this.selectionMode === SelectionMode.SINGLE) {
            updateSelectionWithSingleMode(dayData)
        } else if (this.selectionMode === SelectionMode.MULTIPLE) {
            updateSelectionWithMultipleMode(dayData)
        } else if (this.selectionMode === SelectionMode.RANGE) {
            updateSelectionWithRangeMode(dayData)
        }

        if (this.calendarCallback != null) {
            this.calendarCallback?.onDateSelected(dayData)
        }
    }

    override fun setLayoutManager(layout: RecyclerView.LayoutManager?) {
        super.setLayoutManager(layout)
        this.layoutManager = layout as LinearLayoutManager?
    }

    fun setCalendarCallback(calendarCallback: CalendarCallback) {
        this.calendarCallback = calendarCallback
    }

    private fun init(): CalendarPickerRecyclerView {
        this.adapter = MonthRecyclerViewAdapter(this.context)
        this.adapter?.setHasStableIds(true)
        this.adapter?.setCallback(this)
        setAdapter(this.adapter)

        val nextYear = Calendar.getInstance(timeZone, locale)
        nextYear.add(MONTH, 6)

        this.fromCal = Calendar.getInstance().calendarWith(Date(), timeZone, locale)
        this.fromCal?.add(MONTH, -6)

        this.toCal = Calendar.getInstance().calendarWith(nextYear.time, timeZone, locale)

        return this
    }

    fun loadContent(): CalendarPickerRecyclerView {
        reset()
        this.fromCal?.let {
            var from = it
            this.toCal?.let {
                var to = it
                this.fromCal?.set(Calendar.DAY_OF_MONTH, from.getActualMinimum(Calendar.DAY_OF_MONTH))
                this.toCal?.set(Calendar.DAY_OF_MONTH, to.getActualMaximum(Calendar.DAY_OF_MONTH))

                this.monthCounter = Calendar.getInstance(timeZone, locale)
                monthCounter?.time = this.fromCal?.time

                this.fromCal?.clearHoursMinSecMillsFromCalendar()
                this.toCal?.clearHoursMinSecMillsFromCalendar()

                this.monthCounter?.let {
                    val maxMonth = this.toCal?.get(MONTH)
                    val maxYear = this.toCal?.get(YEAR)
                    while ((monthCounter!!.get(MONTH) <= maxMonth!! || monthCounter!!.get(YEAR) < maxYear!!) && monthCounter!!.get(YEAR) < maxYear!! + 1) {
                        val date = monthCounter!!.time
                        val monthData = MonthData(monthCounter!!.get(MONTH), monthCounter!!.get(YEAR), date, formatMonthDate(date), getDaysFromMonth(date))
                        monthData.setDisplayWeekDays(this.canDisplayWeekDays)
                        monthData.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays)
                        this.monthDataList.add(monthData)
                        this.monthCounter?.add(MONTH, 1)
                    }

                    this.adapter?.monthDataList = monthDataList
                    if (this.dayViewAdapterCallback == null) {
                        this.dayViewAdapterCallback = DefaultDayDataView(this.mContext!!)
                    }
                    this.adapter?.setDayViewAdapterCallback(this.dayViewAdapterCallback!!)
                    this.adapter?.setLocale(this.locale)
                    this.adapter?.setForceDisplayRTL(this.isForceDisplayRTL)
                    this.updateMonthsMap(this.monthDataList)

                    setScrollListener()

                    this.adapter?.notifyDataSetChanged()
                    this.scrollToMonthWithDate(Date())
                }
            }
        }

        return this
    }

    private fun setScrollListener() {
        ShaValidationUtils.checkInfiniteLoad(this.customScrollListener, this.isLoadInfinite)
        if (this.isLoadInfinite) {
            if (this.customScrollListener == null) {
                this.addOnScrollListener(object : RecyclerViewEndlessScrollListener(this.layoutManager!!) {

                    override fun onLoadMorePrevious() {
                        loadPreviousMonths()
                    }

                    override fun onLoadMoreNext() {
                        loadNextMonths()
                    }
                })
            } else {
                this.addOnScrollListener(this.customScrollListener!!)
            }
        }
    }

    fun load(from: Calendar, to: Calendar): CalendarPickerRecyclerView {
        ShaValidationUtils.checkCalendarNull(from)
        ShaValidationUtils.checkCalendarNull(to)
        ShaValidationUtils.checkFromToCalendar(from, to)

        this.fromCal = from
        this.toCal = to
        loadContent()
        return this
    }

    fun load(from: Date): CalendarPickerRecyclerView {
        val to = Calendar.getInstance()
        to.time = from
        load(from, to.time)
        return this
    }

    fun load(from: Calendar): CalendarPickerRecyclerView {
        val to = Calendar.getInstance()
        to.time = from.time
        load(from, to)
        return this
    }

    fun load(from: Date, to: Date): CalendarPickerRecyclerView {
        ShaValidationUtils.checkDateNull(from)
        ShaValidationUtils.checkDateNull(to)
        ShaValidationUtils.checkFromToDates(from, to)

        this.fromCal = Calendar.getInstance().calendarWith(from, timeZone, locale)
        this.toCal = Calendar.getInstance().calendarWith(to, timeZone, locale)
        loadContent()
        return this
    }

    private fun getDaysFromMonth(date: Date): MutableList<DayData> {
        val calMax = Calendar.getInstance(timeZone, locale)
        calMax.time = date
        calMax.set(DAY_OF_MONTH, calMax.getActualMinimum(DAY_OF_MONTH))

        val cal = Calendar.getInstance(timeZone, locale)
        cal.time = date
        cal.set(DAY_OF_MONTH, cal.getActualMinimum(DAY_OF_MONTH))

        val dayDataList = ArrayList<DayData>()

        for (i in 0 .. (calMax.getActualMaximum(DAY_OF_MONTH) - 1)) {
            val dayData = DayData()
            dayData.date = cal.time
            dayData.isToday = cal.time.isSameDay(Calendar.getInstance(timeZone, locale).time)
            dayData.value = cal.get(DAY_OF_MONTH).toString()
            dayData.isCurrentMonth = true

            dayDataList.add(dayData)

            cal.add(DATE, 1)
        }

        return dayDataList
    }

    private fun updateSelectionWithSingleMode(dayData: DayData) {
        if (this.selectedDates.size == 0) {
            this.selectedDates.add(0, dayData)
            updateDayDataListIntoMonthData(dayData, true)

        } else {
            val alreadySelectedDayData = this.selectedDates[0]
            if (dayData.date.isSameDay(alreadySelectedDayData.date)) {
                updateDayDataListIntoMonthData(dayData, false)
                this.selectedDates.clear()
            } else {
                updateDayDataListIntoMonthData(alreadySelectedDayData, false)
                this.selectedDates.add(0, dayData)
                updateDayDataListIntoMonthData(dayData, true)
            }
        }
    }

    private fun updateSelectionWithMultipleMode(dayData: DayData) {
        if (this.selectedDates.size > 0) {
            val alreadySelectedDayDataPosition = this.selectedDates.indexOf(dayData)
            if (alreadySelectedDayDataPosition != -1 &&
                    this.selectedDates[alreadySelectedDayDataPosition].date.isSameDay(dayData.date)) {

                this.selectedDates.removeAt(alreadySelectedDayDataPosition)
                updateDayDataListIntoMonthData(dayData, false)
                return
            }
        }

        this.selectedDates.add(dayData)
        updateDayDataListIntoMonthData(dayData, true)
    }

    private fun updateSelectionWithRangeMode(dayData: DayData) {
        if (this.fromDayData == null) {
            this.fromDayData = dayData
            updateDayDataListIntoMonthData(dayData, true)
            return
        }

        if (this.fromDayData!!.date.isSameDay(dayData.date)) {
            if (this.selectedDateRanges.size > 0) {
                updateDayDataRange(this.selectedDateRanges[0].fromDate!!.date, this.selectedDateRanges[0].toDate!!.date, false)
            }
            updateDayDataListIntoMonthData(dayData, true)
            this.toDayData = null
            return
        }

        if (this.fromDayData!!.date.isSameDay(dayData.date)) {
            if (this.selectedDateRanges.size > 0) {
                updateDayDataRange(this.selectedDateRanges[0].fromDate!!.date, this.selectedDateRanges[0].toDate!!.date, false)
            }

            updateDayDataListIntoMonthData(this.fromDayData!!, false)
            updateDayDataListIntoMonthData(dayData, true)
            this.fromDayData = dayData
            this.toDayData = null
            return
        }

        if (this.toDayData == null) {
            this.toDayData = dayData
            updateDayDataRange(this.fromDayData!!.date, this.toDayData!!.date, true)
            this.selectedDateRanges.add(DateRange(this.fromDayData!!, this.toDayData!!))
            return
        }

        if (this.fromDayData != null && this.toDayData != null) {
            this.selectedDateRanges.clear()
            updateDayDataRange(this.fromDayData!!.date, this.toDayData!!.date, false)
            updateDayDataListIntoMonthData(dayData, true)
            this.fromDayData = dayData
            this.toDayData = null
        }
    }

    private fun updateDayDataRange(from: Date, to: Date, selection: Boolean) {
        this.selectedDateRanges.clear()
        val updatedMonthPositions = ArrayList<Int>()

        val dayDataCalendarFrom = Calendar.getInstance()
        dayDataCalendarFrom.time = from

        while (!dayDataCalendarFrom.time.isAfterDay(to)) {
            val monthDataFrom = this.monthDataMap[dayDataCalendarFrom.monthKey()]

            val dayData = monthDataFrom!!.getDayDataMap()[dayDataCalendarFrom.dayOfMonthWith()]
            if (dayData != null) {
                val dayDataIndexInMonth = monthDataFrom.getDayDataList().indexOf(dayData)
                if (dayDataIndexInMonth != -1) {
                    for (i in dayDataIndexInMonth .. monthDataFrom.getDayDataList().size) {

                        val updateDayData = monthDataFrom.getDayDataList()[i]
                        updateDayData.isSelected = selection
                        updateDayData.rangeState = getRangeState(updateDayData.date, from, to, selection)
                        monthDataFrom.getDayDataList()[i] = updateDayData
                        this.selectedDates.add(updateDayData)
                        dayDataCalendarFrom.add(DAY_OF_MONTH, 1)

                        if (updateDayData.date.isLastDayOfMonth() || updateDayData.date.isSameDay(to)) {
                            updatedMonthPositions.add(this.monthDataList.indexOf(monthDataFrom))
                            break
                        }
                    }
                }
            }
        }

        for (position in updatedMonthPositions) {
            this.adapter!!.notifyItemChanged(position)
        }
    }

    private fun getRangeState(updatedDate: Date, from: Date, to: Date, selection: Boolean): RangeState {
        if (!selection) {
            return RangeState.NONE
        }

        return if (updatedDate.isSameDay(from)) {
            RangeState.FIRST
        } else if (updatedDate.isSameDay(to)) {
            RangeState.LAST
        } else if (updatedDate.isAfterDay(from) && updatedDate.isBeforeDay(to)) {
            RangeState.MIDDLE
        } else {
            RangeState.NONE
        }
    }

    private fun updateDateRangeState(selection: Boolean) {
        if (selection) {
            if (this.selectedDates.size > 2) {
                this.selectedDates[0].rangeState = RangeState.FIRST
                this.selectedDates[this.selectedDates.size - 1].rangeState = RangeState.LAST

                for (i in 1 .. this.selectedDates.size - 1) {
                    this.selectedDates[i].rangeState = RangeState.MIDDLE
                }
            }
        } else {
            if (this.selectedDates.size > 0) {
                for (dayData in this.selectedDates) {
                    dayData.rangeState = RangeState.NONE
                }

                this.selectedDates.clear()
            }
        }
    }

    private fun updateDayDataListIntoMonthData(dayData: DayData, selection: Boolean) {
        dayData.isSelected = selection
        val calendar = Calendar.getInstance()
        calendar.time = dayData.date
        val monthData = this.monthDataMap[dayData.date.monthKey()]
        if (monthData?.getDayDataList() != null && monthData.getDayDataList().indexOf(dayData) != -1) {
            monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData)
            this.adapter?.notifyItemChanged(monthDataList.indexOf(monthData))
        }
    }

    private fun updateMonthsMap(monthsDataList: MutableList<MonthData>) {
        if (!monthsDataList.isEmpty()) {
            for (monthData in monthsDataList) {
                monthData?.date?.monthKey()?.let { this.monthDataMap[it] = monthData }

            }
        }
    }

    fun selectDate(date: Date) {
        selectDateInMonth(date, true)
    }

    fun clearDateSelection(date: Date) {
        selectDateInMonth(date, false)
    }

    fun selectDates(from: Date, to: Date) {
        ShaValidationUtils.checkDateNull(from)
        ShaValidationUtils.checkDateNull(to)
        ShaValidationUtils.checkSelectDates(from, to, this.selectionMode)
        ShaValidationUtils.checkSelectDateWithinCalendarRange(from, fromCal!!, this.toCal!!)
        ShaValidationUtils.checkSelectDateWithinCalendarRange(to, fromCal!!, this.toCal!!)

        if (from.isSameDay(to)) {
            selectDate(from)
            return
        }


        updateDayDataRange(from, to, true)
    }

    fun selectDates(from: Calendar, to: Calendar) {
        ShaValidationUtils.checkCalendarNull(from)
        ShaValidationUtils.checkCalendarNull(to)
        ShaValidationUtils.checkSelectDates(from.time, to.time, this.selectionMode)

        if (from.time.isSameDay(to.time)) {
            selectDate(from.time)
            return
        }

        updateDayDataRange(from.time, to.time, true)
    }

    fun highlightDates(date: Date) {
        highlightDates(date, true)
    }

    fun clearHighlightDate(date: Date) {
        highlightDates(date, false)
    }

    fun highlightDates(dates: Collection<Date>) {
        for (date in dates) {
            highlightDates(date)
        }
    }

    fun clearHighlightDates(dates: Collection<Date>) {
        for (date in dates) {
            clearHighlightDate(date)
        }
    }

    private fun highlightDates(date: Date, isHighLighted: Boolean) {
        ShaValidationUtils.checkHighlightDate(date, fromCal!!, this.toCal!!)

        updateMonthByDate(date, isHighLighted)
    }

    fun scrollToMonthWithDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val monthData = this.monthDataMap[date.monthKey()]
        if (monthData != null) {
            val monthIndex = this.monthDataList.indexOf(monthData)
            if (monthIndex != -1) {
                scrollToPosition(monthIndex)
            }
        }
    }

    private fun updateMonthByDate(date: Date, isHighLighted: Boolean) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val monthData = this.monthDataMap[date.monthKey()]
        val dayData = monthData!!.getDayDataMap()[date.dayOfMonthWith()]
        if (dayData != null) {
            val indexOfDayInMonth = monthData.getDayDataList().indexOf(dayData)
            if (indexOfDayInMonth == -1) {
                return
            }

            dayData!!.isHighlighted = isHighLighted
            monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData)
            this.adapter!!.notifyItemChanged(monthDataList.indexOf(monthData))
        }
    }

    private fun selectDateInMonth(date: Date, selection: Boolean) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val monthData = this.monthDataMap[date.monthKey()]
        val dayData = monthData!!.getDayDataMap()[date.dayOfMonthWith()]
        if (dayData != null) {
            val indexOfDayInMonth = monthData.getDayDataList().indexOf(dayData)
            if (indexOfDayInMonth == -1) {
                return
            }

            dayData!!.isSelected = selection
            monthData.getDayDataList().set(monthData.getDayDataList().indexOf(dayData), dayData)
            this.adapter?.notifyItemChanged(monthDataList.indexOf(monthData))
        }
    }

    private fun formatMonthDate(date: Date): String {
        val dateFormatted: String
        val sb = StringBuilder()
        val sdfMonth = SimpleDateFormat(getContext()
                .getString(R.string.month_only_name_format), locale)
        val sdfYear = SimpleDateFormat(getContext()
                .getString(R.string.year_only_format), Locale.ENGLISH)
        dateFormatted = sb.append(sdfMonth.format(date.time)).append(" ")
                .append(sdfYear.format(date.time)).toString()
        return dateFormatted
    }

    private fun clearData() {
        monthDataList.clear()
        selectedDates.clear()
        selectedDateRanges.clear()
        monthDataMap.clear()
    }

    private fun loadMoreMonths(from: Calendar, to: Calendar, addIndexPosition: Int) {
        val loadMoreMonthDataList = ArrayList<MonthData>()
        val monthCounter = Calendar.getInstance(timeZone, locale)
        monthCounter.time = from.time

        from.clearHoursMinSecMillsFromCalendar()
        to.clearHoursMinSecMillsFromCalendar()

        val maxMonth = to.get(MONTH)
        val maxYear = to.get(YEAR)
        while ((monthCounter.get(MONTH) <= maxMonth || monthCounter.get(YEAR) < maxYear) && monthCounter.get(YEAR) < maxYear + 1) {
            val date = monthCounter.time
            val monthData = MonthData(monthCounter.get(MONTH), monthCounter.get(YEAR),
                    date, formatMonthDate(date), getDaysFromMonth(date))
            monthData.setDisplayWeekDays(this.canDisplayWeekDays)
            monthData.setDisplayPreviousNextMonthDays(this.canDisplayPreviousNextMonthDays)
            loadMoreMonthDataList.add(monthData)
            monthCounter.add(MONTH, 1)
        }

        if (loadMoreMonthDataList.size > 0) {
            this.monthDataList.addAll(addIndexPosition, loadMoreMonthDataList)
        }

        this.updateMonthsMap(loadMoreMonthDataList)
    }

    private fun loadPreviousMonths() {
        val from = Calendar.getInstance()
        from.time = this.fromCal!!.time
        from.add(MONTH, -defaultLoadMonthsWithInfiniteLoad)
        from.set(DAY_OF_MONTH, from.getActualMinimum(DAY_OF_MONTH))

        val to = Calendar.getInstance()
        to.time = this.fromCal!!.time
        to.add(MONTH, -1)
        to.set(DAY_OF_MONTH, to.getActualMaximum(DAY_OF_MONTH))

        this.fromCal = from

        loadMoreMonths(from, to, 0)
        this.adapter!!.notifyItemRangeInserted(0, defaultLoadMonthsWithInfiniteLoad)
    }

    private fun loadNextMonths() {
        val from = Calendar.getInstance()
        from.time = this.toCal!!.time
        from.add(MONTH, 1)
        from.set(DAY_OF_MONTH, from.getActualMinimum(DAY_OF_MONTH))

        val to = Calendar.getInstance()
        to.time = this.toCal!!.time
        to.add(DAY_OF_MONTH, defaultLoadMonthsWithInfiniteLoad)
        to.set(DAY_OF_MONTH, to.getActualMaximum(DAY_OF_MONTH))

        this.toCal = to

        val currentScrollPosition = this.monthDataList.size
        loadMoreMonths(from, to, this.monthDataList.size)

        this.adapter?.notifyItemRangeInserted(currentScrollPosition, defaultLoadMonthsWithInfiniteLoad)
    }


    fun initSettings(): CalendarSettings {
        return CalendarSettings()
    }

    inner class CalendarSettings {
        private var canDisplayWeekDays = true
        private var canDisplayPreviousNextMonthDays = false
        private var canDisplayBorders = false
        private var isForceDisplayRTL = false
        private var isLoadInfinite = false
        private var selectionMode = SelectionMode.SINGLE
        private var dayViewAdapterCallback: IDayViewAdapterCallback? = null
        private var locale = Locale.getDefault()
        private var timeZone = TimeZone.getDefault()
        private var customScrollListener: RecyclerView.OnScrollListener? = null

        fun setDisplayWeekDaysByMonth(canDisplayWeekDays: Boolean): CalendarSettings {
            this.canDisplayWeekDays = canDisplayWeekDays
            return this
        }

        fun setDisplayPreviousNextMonthDaysByMonth(canDisplayPreviousNextMonthDays: Boolean): CalendarSettings {
            this.canDisplayPreviousNextMonthDays = canDisplayPreviousNextMonthDays
            return this
        }

        fun setSelectionMode(selectionMode: SelectionMode): CalendarSettings {
            this.selectionMode = selectionMode
            return this
        }

        fun setCanDisplayBorders(canDisplayBorders: Boolean): CalendarSettings {
            this.canDisplayBorders = canDisplayBorders
            return this
        }

        fun setDayViewAdapterCallback(dayViewAdapterCallback: IDayViewAdapterCallback): CalendarSettings {
            this.dayViewAdapterCallback = dayViewAdapterCallback
            return this
        }

        fun setForceDisplayRTL(isForceDisplayRTL: Boolean): CalendarSettings {
            this.isForceDisplayRTL = isForceDisplayRTL
            return this
        }

        fun setLoadInfinite(loadInfinite: Boolean): CalendarSettings {
            isLoadInfinite = loadInfinite
            return this
        }

        fun setCustomScrollListener(customScrollListener: RecyclerView.OnScrollListener): CalendarSettings {
            this.customScrollListener = customScrollListener
            return this
        }

        fun setLocale(locale: Locale?): CalendarSettings {
            if (locale == null) {
                this.locale = Locale.getDefault()
                return this
            }

            this.locale = locale

            return this
        }

        fun setTimeZone(timeZone: TimeZone?): CalendarSettings {
            if (timeZone == null) {
                this.timeZone = TimeZone.getDefault()
                return this
            }

            this.timeZone = timeZone
            return this
        }

        fun apply(): CalendarPickerRecyclerView {
            this@CalendarPickerRecyclerView.canDisplayWeekDays = this.canDisplayWeekDays
            this@CalendarPickerRecyclerView.canDisplayPreviousNextMonthDays = this.canDisplayPreviousNextMonthDays
            this@CalendarPickerRecyclerView.selectionMode = this.selectionMode
            this@CalendarPickerRecyclerView.canDisplayBorders = this.canDisplayBorders
            this@CalendarPickerRecyclerView.isForceDisplayRTL = this.isForceDisplayRTL
            this@CalendarPickerRecyclerView.isLoadInfinite = this.isLoadInfinite
            this@CalendarPickerRecyclerView.locale = this.locale
            this@CalendarPickerRecyclerView.timeZone = this.timeZone
            this@CalendarPickerRecyclerView.dayViewAdapterCallback = this.dayViewAdapterCallback
            this@CalendarPickerRecyclerView.customScrollListener = this.customScrollListener
            return this@CalendarPickerRecyclerView
        }
    }

    interface CalendarCallback {
        fun onDateSelected(dayData: IDayData)
    }
}
