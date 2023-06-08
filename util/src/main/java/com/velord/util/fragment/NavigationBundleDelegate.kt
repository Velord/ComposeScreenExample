package com.velord.util.fragment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.velord.util.navigation.NavigationData
import kotlin.reflect.KProperty

const val DEFAULT_BUNDLE_NAME = "navigationData"

interface NavigationBundleDelegate<out T, in R> {
    operator fun provideDelegate(thisRef: R, prop: KProperty<*>): Lazy<T>
}

inline fun <reified T, R: LifecycleOwner> R.createNavigationBundleDelegate(
    crossinline getBundle: R.() -> Bundle
): NavigationBundleDelegate<T, R> = object : NavigationBundleDelegate<T, R> {
    override fun provideDelegate(thisRef: R, prop: KProperty<*>): Lazy<T> = lazy {
        val bundle = thisRef.getBundle()
        val name = T::class.simpleName ?: DEFAULT_BUNDLE_NAME
        bundle[name] as T
    }
}

inline fun <reified T, reified R: Fragment> R.getNavigationData(): NavigationBundleDelegate<T, R> =
    createNavigationBundleDelegate { requireArguments() }

inline fun <reified T, R: ComponentActivity> R.getNavigationData(): NavigationBundleDelegate<T, R> =
    createNavigationBundleDelegate {
        intent.extras ?: throw IllegalStateException("Activity $this does not have any arguments.")
    }

fun NavController.navigate(nav: NavigationData) {
    val bundle: Bundle? = if (nav.data != null) {
        val name = nav.data::class.simpleName ?: DEFAULT_BUNDLE_NAME
        bundleOf(name to nav.data)
    } else {
        null
    }
    navigate(nav.id, bundle)
}



