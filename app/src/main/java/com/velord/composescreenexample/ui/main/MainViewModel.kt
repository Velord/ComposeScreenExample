package com.velord.composescreenexample.ui.main

import androidx.lifecycle.SavedStateHandle
import com.velord.backend.service.auth.AuthService
import com.velord.datastore.DataStoreService
import com.velord.util.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authService: AuthService,
    private val dataStoreService: DataStoreService,
) : BaseViewModel() {

    init {

    }
}