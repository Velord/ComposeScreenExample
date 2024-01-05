package com.velord.navigation

import android.os.Parcelable

data class NavigationData(
    val screen: SharedScreen,
    val data: Parcelable? = null
)