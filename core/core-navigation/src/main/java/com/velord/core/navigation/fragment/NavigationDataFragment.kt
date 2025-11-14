package com.velord.core.navigation.fragment

import android.os.Bundle

data class NavigationDataFragment(
    val id: Int,
    val bundle: Bundle? = null // Was Parcelable
)