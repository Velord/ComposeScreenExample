package com.velord.ui.feature.demo.hintphonenumber

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velord.core.resource.Res
import com.velord.core.resource.invoke_again
import com.velord.core.resource.phone_number_hint_not_available_on_desktop
import com.velord.core.resource.waiting
import com.velord.core.ui.compose.preview.PreviewCombined
import org.jetbrains.compose.resources.stringResource

@Composable
fun HintPhoneNumberScreen() {
    val phoneState: MutableState<String?> = remember {
        mutableStateOf(null)
    }

    val unavailableMessage = stringResource(Res.string.phone_number_hint_not_available_on_desktop)
    val register = rememberPhoneNumberHintLauncher { result ->
        phoneState.value = when (result) {
            is PhoneNumberHintResult.Hint -> result.phoneNumber
            is PhoneNumberHintResult.Failure -> result.message
            PhoneNumberHintResult.Unavailable -> unavailableMessage
        }
    }
    if (phoneState.value == null) register()

    Content(
        phone = phoneState.value,
        register = register
    )
}

@Composable
private fun Content(
    phone: String?,
    register: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val defStr = stringResource(Res.string.waiting)
            val str = phone ?: defStr
            Text(
                text = str,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )

            Button(
                onClick = { register() },
                modifier = Modifier.padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                )
            ) {
                Text(
                    text = stringResource(Res.string.invoke_again),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Content(
        phone = "+1234567890",
        register = { }
    )
}
