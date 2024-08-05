package com.velord.util.navigation

import android.os.Parcelable

data class NavigationData(
    val id: Int,
    val data: Parcelable? = null
)