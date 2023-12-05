package com.velord.composescreenexample.ui.main.inDevelopment

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
class InDevelopmentViewModel @Inject constructor(
    val validateUserNameUseCase: ValidateUserNameUseCase
): CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    init {
        validateUserNameUseCase.inv()
        Log.d("!!!", "InDevelopmentViewModel init: ${this::class}")
    }

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationData(R.id.toInDevelopmentFragment))
    }
}