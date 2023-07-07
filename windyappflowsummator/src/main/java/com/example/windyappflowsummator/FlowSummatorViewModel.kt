package com.example.windyappflowsummator

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FlowSummatorViewModel : BaseViewModel() {

    val state: MutableStateFlow<Int> = MutableStateFlow(0)

    fun onEvent() {

    }
}