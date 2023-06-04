package com.velord.util.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

fun Fragment.activityNavController() =
    activity?.supportFragmentManager?.fragments?.firstOrNull()?.findNavController()