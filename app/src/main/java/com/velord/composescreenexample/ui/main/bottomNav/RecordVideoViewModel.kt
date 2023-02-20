package com.velord.composescreenexample.ui.main.bottomNav

import com.velord.composescreenexample.utils.PermissionState
import com.velord.composescreenexample.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordVideoViewModel @Inject constructor(
) : BaseViewModel() {

    val permissionFlow = MutableStateFlow(PermissionState.NotAsked)
    val goToSettingsEvent = MutableSharedFlow<Unit>()
    val checkPermissionEvent = MutableSharedFlow<Unit>()

    fun updatePermissionState(state: PermissionState) {
        permissionFlow.value = state
    }

    fun onGoToSettingsClick() = launch {
        goToSettingsEvent.emit(Unit)
    }

    fun onCheckPermission() = launch {
        checkPermissionEvent.emit(Unit)
    }
}