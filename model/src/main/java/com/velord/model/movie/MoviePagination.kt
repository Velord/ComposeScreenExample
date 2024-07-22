package com.velord.model.movie

data object MoviePagination {
    private const val PRELOAD_BEFORE_END = 10
    const val PAGE_COUNT = 20

    fun shouldLoadMore(
        lastVisibleIndex: Int,
        totalItemCount: Int,
    ): Boolean {
        if (lastVisibleIndex == 0 && totalItemCount == 0) error("Invalid index and total count")

        return lastVisibleIndex >= totalItemCount - PRELOAD_BEFORE_END
    }
}