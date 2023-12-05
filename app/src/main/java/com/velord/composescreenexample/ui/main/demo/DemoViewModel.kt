package com.velord.composescreenexample.ui.main.demo

import android.util.Log
import com.example.sharedviewmodel.CoroutineScopeViewModel
import com.velord.composescreenexample.R
import com.velord.composescreenexample.di.ValidateUserNameUseCase
import com.velord.util.navigation.NavigationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    val validateUserNameUseCase: ValidateUserNameUseCase
): CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    init {
        validateUserNameUseCase.inv()
        Log.d("!!!", "DemoViewModel init: ${this}")
    }

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