package com.velord.util.activityResult

import android.app.PendingIntent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

private val request = GetPhoneNumberHintIntentRequest.builder().build()
private val resultLauncher: ComponentActivity.(
        (phoneNumber: String) -> Unit,
        (e: Exception) -> Unit
) -> ActivityResultLauncher<IntentSenderRequest> = { onHint, onError ->
    registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
            Log.d(this.localClassName,"Phone Number: $phoneNumber")
            onHint(phoneNumber)
        } catch(e: Exception) {
            Log.d(this.localClassName, "Phone Number Hint failed")
            onError(e)
        }
    }
}

fun ComponentActivity.registerPhoneNumberHint(
    onExceptionLaunch: (e: Exception) -> Unit,
    onFailure: (e: Exception) -> Unit,
    onHint: (phoneNumber: String) -> Unit,
    onHintError: (e: Exception) -> Unit
) : () -> Unit {
    val launcher = resultLauncher(onHint, onHintError)
    return {
        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    launcher.launch(IntentSenderRequest.Builder(result).build())
                } catch (e: Exception) {
                    Log.d(this.localClassName, "Launching the PendingIntent failed")
                    onExceptionLaunch(e)
                }
            }
            .addOnFailureListener { e ->
                Log.d(this.localClassName, "Failure: ${e.message}")
                onFailure(e)
            }
    }
}

@Composable
fun registerPhoneNumberHint(
    activity: ComponentActivity,
    onExceptionLaunch: (e: Exception) -> Unit,
    onFailure: (e: Exception) -> Unit,
    onHint: (phoneNumber: String) -> Unit,
    onHintError: (e: Exception) -> Unit
) : () -> Unit {
    val launcher = activity.phoneNumberHintIntentResultLauncher(onHint, onHintError)
    return {
        Identity.getSignInClient(activity)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    launcher.launch(IntentSenderRequest.Builder(result).build())
                } catch (e: Exception) {
                    Log.d(activity.localClassName, "Launching the PendingIntent failed")
                    onExceptionLaunch(e)
                }
            }
            .addOnFailureListener { e ->
                Log.d(activity.localClassName, "Failure: ${e.message}")
                onFailure(e)
            }
    }
}

// used inside of a composable(for outside use registerForActivityResult instead)
val phoneNumberHintIntentResultLauncher
: @Composable ComponentActivity.(
        (phoneNumber: String) -> Unit,
        (e: Exception) -> Unit
) -> ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> = { onPhone, onError ->
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
            Log.d(this.localClassName,"Phone Number: $phoneNumber")
            onPhone(phoneNumber)
        } catch (e: Exception) {
            onError(e)
            Log.d(this.localClassName, "Phone Number Hint failed")
        }
    }
}