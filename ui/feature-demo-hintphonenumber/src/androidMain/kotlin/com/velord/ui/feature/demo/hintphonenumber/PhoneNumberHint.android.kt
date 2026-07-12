package com.velord.ui.feature.demo.hintphonenumber

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import com.velord.infrastructure.util.activityResult.registerPhoneNumberHint

@Composable
internal actual fun rememberPhoneNumberHintLauncher(
    onResult: (PhoneNumberHintResult) -> Unit,
): () -> Unit {
    val activity = LocalActivity.current as ComponentActivity

    return registerPhoneNumberHint(
        activity = activity,
        onExceptionLaunch = { exception ->
            onResult(PhoneNumberHintResult.Failure(exception.message))
        },
        onFailure = { exception ->
            onResult(PhoneNumberHintResult.Failure(exception.message))
        },
        onHint = { phoneNumber ->
            onResult(PhoneNumberHintResult.Hint(phoneNumber))
        },
        onHintError = { exception ->
            onResult(PhoneNumberHintResult.Failure(exception.message))
        },
    )
}
