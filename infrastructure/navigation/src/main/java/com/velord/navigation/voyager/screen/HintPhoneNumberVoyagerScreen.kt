package com.velord.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.hintphonenumber.HintPhoneNumberScreen

internal object HintPhoneNumberVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        HintPhoneNumberScreen()
    }
}