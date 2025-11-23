package com.velord.navigation.jetpackNavigation.inDevelopment

import com.velord.core.navigation.fragment.NavigationDataFragment
import com.velord.navigation.R
import com.velord.sharedviewmodel.CoroutineScopeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InDevelopmentViewModel : CoroutineScopeViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationDataFragment>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationDataFragment(R.id.toInDevelopmentFragment))
    }
}