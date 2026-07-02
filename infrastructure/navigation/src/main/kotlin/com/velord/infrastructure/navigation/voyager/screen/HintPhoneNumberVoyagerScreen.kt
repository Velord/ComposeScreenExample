package com.velord.infrastructure.navigation.voyager.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.velord.ui.feature.demo.hintphonenumber.HintPhoneNumberScreen

internal object HintPhoneNumberVoyagerScreen : Screen {

    @Composable
    override fun Content() {
        HintPhoneNumberScreen()
    }
}
