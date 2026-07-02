package com.velord.infrastructure.config

interface BuildConfigResolver {
    fun getNavigationLib(): NavigationLib
}
