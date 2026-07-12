package com.velord.ui.feature.demo.hintphonenumber

import androidx.compose.runtime.Composable

@Composable
internal actual fun rememberPhoneNumberHintLauncher(
    onResult: (PhoneNumberHintResult) -> Unit,
): () -> Unit = {
    onResult(PhoneNumberHintResult.Unavailable)
}
