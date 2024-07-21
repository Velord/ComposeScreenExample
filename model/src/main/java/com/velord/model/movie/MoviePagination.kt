package com.velord.model.movie

data object MoviePagination {
    private const val PRELOAD_BEFORE_END = 10
    const val PAGE_COUNT = 20

    fun shouldLoadMore(
        lastVisibleIndex: Int,
        totalItemCount: Int,
    ): Boolean = lastVisibleIndex >= totalItemCount - PRELOAD_BEFORE_END
}