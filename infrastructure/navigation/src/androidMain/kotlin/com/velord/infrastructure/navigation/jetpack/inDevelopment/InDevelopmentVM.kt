package com.velord.infrastructure.navigation.jetpack.inDevelopment

import com.velord.core.navigation.fragment.NavigationDataFragment
import com.velord.infrastructure.navigation.R
import com.velord.ui.sharedviewmodel.CoroutineScopeVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Emits the recursive Jetpack navigation event used by [InDevelopmentFragment].
 */
class InDevelopmentVM : CoroutineScopeVM() {

    val navigationEvent = MutableSharedFlow<NavigationDataFragment>()
    private val actionFlow = MutableSharedFlow<InDevelopmentUiAction>()

    init {
        observe()
    }

    fun onAction(action: InDevelopmentUiAction) {
        launch {
            actionFlow.emit(action)
        }
    }

    private fun onOpenNew() = launch {
        navigationEvent.emit(NavigationDataFragment(R.id.toInDevelopmentFragment))
    }

    private fun observe() {
        launch {
            actionFlow.collect { action ->
                when (action) {
                    is InDevelopmentUiAction.OpenNew -> onOpenNew()
                }
            }
        }
    }
}
