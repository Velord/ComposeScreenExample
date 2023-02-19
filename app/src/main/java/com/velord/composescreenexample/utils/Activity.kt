package com.velord.composescreenexample.utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.velord.composescreenexample.R

private const val DELAY_TO_EXIT_APP = 3000L

private var timeSnapshot: Long = 0

context(Activity)
fun View.showSnackbarOrFinish() {
    if (timeSnapshot + DELAY_TO_EXIT_APP > System.currentTimeMillis()) finish()
    else Snackbar.make(this, R.string.press_again_to_exit, Snackbar.LENGTH_SHORT).show()

    timeSnapshot = System.currentTimeMillis()
}