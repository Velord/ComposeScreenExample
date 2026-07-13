package com.velord.infrastructure.navigation.jetpackNavigation.inDevelopment

import com.velord.core.navigation.fragment.NavigationDataFragment
import com.velord.infrastructure.navigation.R
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InDevelopmentVM : CoroutineScopeVM() {

    val navigationEvent = MutableSharedFlow<NavigationDataFragment>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationDataFragment(R.id.toInDevelopmentFragment))
    }
}
