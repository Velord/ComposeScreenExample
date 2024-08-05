package com.velord.navigation.jetpackNavigation.inDevelopment

import com.velord.navigation.R
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import com.velord.util.navigation.NavigationData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InDevelopmentViewModel : CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationData(R.id.toInDevelopmentFragment))
    }
}