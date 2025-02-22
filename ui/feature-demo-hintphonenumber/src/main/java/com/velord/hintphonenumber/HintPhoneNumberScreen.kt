package com.velord.hintphonenumber

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.velord.resource.R
import com.velord.uicore.compose.preview.PreviewCombined
import com.velord.util.activityResult.registerPhoneNumberHint

@Composable
fun HintPhoneNumberScreen() {
    val phoneState: MutableState<String?> = remember {
        mutableStateOf(null)
    }

    val activity = LocalActivity.current as ComponentActivity
    val register = registerPhoneNumberHint(
        activity = activity,
        onExceptionLaunch = { e -> phoneState.value = e.message },
        onFailure = { e -> phoneState.value = e.message },
        onHint = { phoneNumber -> phoneState.value = phoneNumber },
        onHintError = { e -> phoneState.value = e.message }
    )
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
            modifier = Modifier.background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val defStr = stringResource(id = R.string.waiting)
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
                    text = stringResource(id = R.string.invoke_again),
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
