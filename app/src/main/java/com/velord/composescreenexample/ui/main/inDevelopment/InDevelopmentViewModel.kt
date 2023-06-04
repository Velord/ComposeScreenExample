package com.velord.composescreenexample.ui.main.inDevelopment

import com.velord.composescreenexample.R
import com.velord.composescreenexample.shared.navigation.NavigationData
import com.velord.util.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InDevelopmentViewModel @Inject constructor(
) : BaseViewModel() {

    val navigationEvent = MutableSharedFlow<NavigationData>()
    val backEvent = MutableSharedFlow<Unit>()

    fun onOpenNew() = launch {
        navigationEvent.emit(NavigationData(R.id.toInDevelopmentFragment))
    }

    fun onBackPressed() = launch {
        backEvent.emit(Unit)
    }
}