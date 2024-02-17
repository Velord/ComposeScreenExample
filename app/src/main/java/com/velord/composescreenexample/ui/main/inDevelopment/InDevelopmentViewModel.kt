package com.velord.composescreenexample.ui.main.inDevelopment

import com.example.sharedviewmodel.CoroutineScopeViewModel
import com.velord.composescreenexample.R
import com.velord.util.navigation.NavigationData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InDevelopmentViewModel : CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationData(R.id.toInDevelopmentFragment))
    }
}