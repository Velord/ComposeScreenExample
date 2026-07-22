package com.velord.infrastructure.navigation.creation

import com.velord.core.ui.annotation.ConstructorLikeFunction

@ConstructorLikeFunction
internal fun CreateNavigationViaJetpack(): Nothing = error(
    "Jetpack navigation can no be created inside Compose. Use root View in root Activity."
)