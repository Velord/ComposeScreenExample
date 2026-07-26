package com.velord.ui.feature.demo.hintphonenumber

import androidx.compose.runtime.Composable

internal sealed interface PhoneNumberHintResult {
    data class Hint(val phoneNumber: String) : PhoneNumberHintResult
    data class Failure(val message: String?) : PhoneNumberHintResult
    data object Unavailable : PhoneNumberHintResult
}

@Composable
internal expect fun rememberPhoneNumberHintLauncher(
    onResult: (PhoneNumberHintResult) -> Unit,
): () -> Unit
