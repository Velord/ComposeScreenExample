package com.velord.composescreenexample.ui.main.demo

import com.velord.composescreenexample.R
import com.velord.util.navigation.NavigationData
import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class DemoViewModel : BaseViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    fun onOpenShape() = launch {
        navigationEvent.emit(NavigationData(R.id.from_demoFragment_to_shapeDemoFragment))
    }

    fun onOpenModifier() = launch {
        navigationEvent.emit(NavigationData(R.id.from_demoFragment_to_modifierDemoFragment))
    }

    fun onOpenSummator() = launch {
        navigationEvent.emit(NavigationData(R.id.from_demoFragment_to_flowSummatorFragment))
    }
}