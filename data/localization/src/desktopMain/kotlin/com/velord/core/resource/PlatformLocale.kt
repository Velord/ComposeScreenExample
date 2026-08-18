package com.velord.core.resource

import java.util.Locale

internal actual fun currentLanguageTag(): String = Locale.getDefault().toLanguageTag()
