package com.velord.core.ui.dialog

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.alertDialog(
    cancelable: Boolean = true,
    title: String? = null,
    message: String? = null,
    positiveText: String? = null,
    negativeText: String? = null,
    negativeCallback: (() -> Unit)? = null,
    positiveCallback: () -> Unit = {}
) {
    val dialog = MaterialAlertDialogBuilder(this)
        .setCancelable(cancelable)
        .create()

    positiveText?.let {
        dialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            it
        ) { _, _ ->
            positiveCallback.invoke()
        }
    }
    title?.let(dialog::setTitle)
    message?.let { dialog.setMessage(message) }
    negativeText?.let {
        dialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            it
        ) { _, _ ->
            negativeCallback?.invoke()
        }
    }

    dialog.show()
}
