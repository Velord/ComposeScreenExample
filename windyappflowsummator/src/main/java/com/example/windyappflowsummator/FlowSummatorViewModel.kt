package com.example.windyappflowsummator

import com.velord.util.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class FlowSummatorViewModel : BaseViewModel() {

    val state: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentEnteredValueFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    fun onEvent() {

    }

    fun onStartClick() {

    }

    fun onNewEnteredValue(newValue: String) {
        val calculatedValue = if (newValue.toIntOrNull() == null) {
            // Case when user entered not a number(from clipboard framework)
            // or empty string
            // or that is not a number(BigInteger)
            if (newValue.isEmpty()) null // Case when user cleared the field
            else currentEnteredValueFlow.value
        } else {
            newValue
        }

        currentEnteredValueFlow.value = calculatedValue
    }
}