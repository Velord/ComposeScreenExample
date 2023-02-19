package com.velord.composescreenexample.ui.main

import androidx.lifecycle.SavedStateHandle
import com.velord.backend.service.auth.AuthService
import com.velord.composescreenexample.viewModel.BaseViewModel
import com.velord.datastore.DataStoreService
import com.velord.model.profile.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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