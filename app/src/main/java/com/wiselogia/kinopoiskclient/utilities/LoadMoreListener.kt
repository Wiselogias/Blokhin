package com.wiselogia.kinopoiskclient.utilities

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreListener(private val onLoadMore: () -> Unit) : View.OnScrollChangeListener {
    var isLoading = false
    override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
        if(((p0 as RecyclerView).layoutManager as LinearLayoutManager).findLastVisibleItemPosition() >
            (p0.adapter as RecyclerView.Adapter).itemCount - 4 && !isLoading) {
            onLoadMore()
            isLoading = true
        }
    }

}