package com.velord.data.localization

import java.util.Locale

internal actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()
