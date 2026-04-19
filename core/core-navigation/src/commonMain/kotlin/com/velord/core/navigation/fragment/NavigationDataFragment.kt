package com.velord.core.navigation.fragment

const val NAVIGATION_PAYLOAD = "navigationPayload"

data class NavigationDataFragment(
    val id: Int,
    val payload: String? = null,
)
