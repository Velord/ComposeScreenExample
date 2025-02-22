package com.velord.model.movie

import android.util.Log

data object MoviePagination {
    private const val PRELOAD_BEFORE_END = 10
    const val PAGE_COUNT = 20

    fun shouldLoadMore(
        lastVisibleIndex: Int,
        totalItemCount: Int,
    ): Boolean {
        if (lastVisibleIndex == 0 && totalItemCount == 0) error("Invalid index and total count")

        val shouldLoadMore = lastVisibleIndex >= totalItemCount - PRELOAD_BEFORE_END
        Log.d("Pagination", "shouldLoadMore: $shouldLoadMore")
        return shouldLoadMore
    }

    fun calculateOffset(page: Int): Int =
        if (page < 2) { 0 } else { (page - 1) * PAGE_COUNT }

    fun calculatePage(collectionSize: Int): Int =
        if (collectionSize < PAGE_COUNT) { 1 } else { collectionSize / PAGE_COUNT + 1 }
}