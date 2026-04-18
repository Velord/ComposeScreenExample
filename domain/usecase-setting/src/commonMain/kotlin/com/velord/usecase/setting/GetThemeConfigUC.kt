package com.velord.usecase.setting

import com.velord.model.setting.ThemeConfig
import kotlinx.coroutines.flow.Flow

/**
 * Returns a reactive stream of the current theme configuration.
 *
 * The first invocation may initialize in-memory theme state from persistence. After that,
 * collectors are expected to receive future theme updates through the returned [Flow].
 */
fun interface GetThemeConfigUC : suspend () -> Flow<ThemeConfig>
