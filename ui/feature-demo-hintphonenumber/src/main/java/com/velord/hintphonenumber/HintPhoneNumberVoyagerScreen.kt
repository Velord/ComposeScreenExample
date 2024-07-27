package com.velord.hintphonenumber

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object HintPhoneNumberVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        HintPhoneNumberScreen()
    }
}