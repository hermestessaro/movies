package com.example.movies.util


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var loading = true
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var visibleThreshold: Int = 5
    private var previousTotal = 0
    private var totalItemCount = 0

    private var startPageNumber = 0
    private var currentPage = 0

    private var scrollPosition = 0

    private val linearLayoutManager = layoutManager

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = linearLayoutManager.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItem) >= totalItemCount
                && firstVisibleItem >= 0
            ) {
                loadMoreItems()
            }
        }

    }

    abstract fun getTotalPageCount(): Int
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems()

}