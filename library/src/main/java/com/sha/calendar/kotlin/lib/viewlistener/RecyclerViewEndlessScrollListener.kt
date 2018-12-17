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

package com.sha.calendar.kotlin.lib.viewlistener

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Shahul Hameed Shaik
 * Email: android.shahul@gmail.com
 */
abstract class RecyclerViewEndlessScrollListener(private val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var loading = false
    private var isUserScrolling = false

    private var previousTotal = 0
    private var totalItemCount: Int = 0

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            this.isUserScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = mLinearLayoutManager.itemCount

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (isUserScrolling) {
            val pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
            if (pastVisibleItems == 0) {
                onLoadMorePrevious()
                isUserScrolling = false
                return
            }
        }

        val pos = mLinearLayoutManager.findFirstVisibleItemPosition()
        if (mLinearLayoutManager.itemCount - pos <= 2) {
            onLoadMoreNext()
            loading = true
        }
    }

    abstract fun onLoadMoreNext()
    abstract fun onLoadMorePrevious()
}
