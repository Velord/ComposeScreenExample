package com.velord.composescreenexample.ui.main.bottomNav.centerGraph

import android.util.Log
import com.example.sharedviewmodel.CoroutineScopeViewModel
import com.velord.composescreenexample.di.ValidateUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CenterGraphViewModel @Inject constructor(
    val validateUserNameUseCase: ValidateUserNameUseCase
): CoroutineScopeViewModel() {
    init {
        validateUserNameUseCase.inv()
        Log.d("!!!", "CenterGraphViewModel init: ${this::class}")
    }
}