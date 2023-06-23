package com.velord.composescreenexample.ui.main.inDevelopment

import com.velord.composescreenexample.R
import com.velord.util.navigation.NavigationData
import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InDevelopmentViewModel : BaseViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationData(R.id.toInDevelopmentFragment))
    }
}