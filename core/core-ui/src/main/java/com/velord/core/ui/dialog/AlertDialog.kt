package com.velord.core.ui.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.alertDialog(
    cancelable: Boolean = true,
    @StringRes title: Int? = null,
    @StringRes message: Int? = null,
    @StringRes positiveText: Int? = null,
    @StringRes negativeText: Int? = null,
    negativeCallback: (() -> Unit)? = null,
    positiveCallback: () -> Unit = {}
) {
    val messageStr = message?.let { getString(it) }
    alertDialog(
        cancelable = cancelable,
        title = title,
        message = messageStr,
        positiveText = positiveText,
        negativeText = negativeText,
        negativeCallback = negativeCallback,
        positiveCallback = positiveCallback
    )
}

fun Context.alertDialog(
    cancelable: Boolean = true,
    @StringRes title: Int? = null,
    message: String? = null,
    @StringRes positiveText: Int? = null,
    @StringRes negativeText: Int? = null,
    negativeCallback: (() -> Unit)? = null,
    positiveCallback: () -> Unit = {}
) {
    val dialog = MaterialAlertDialogBuilder(this)
        .setCancelable(cancelable)
        .create()

    positiveText?.let {
        dialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            this.getString(it)
        ) { _, _ ->
            positiveCallback.invoke()
        }
    }
    title?.let { dialog.setTitle(it) }
    message?.let { dialog.setMessage(message) }
    negativeText?.let {
        dialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            this.getString(negativeText)
        ) { _, _ ->
            negativeCallback?.invoke()
        }
    }

    dialog.show()
}