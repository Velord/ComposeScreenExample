package com.velord.ui.feature.splash

import android.os.Build

internal actual val shouldShowBrandIcon: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
