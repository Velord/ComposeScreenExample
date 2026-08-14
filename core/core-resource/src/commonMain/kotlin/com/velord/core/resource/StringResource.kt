package com.velord.core.resource

import androidx.compose.runtime.Composable

@Composable
fun stringResource(
    resource: AppStringResource,
    vararg formatArgs: Any,
): String = LocalizationRuntime.getString(resource, *formatArgs)

fun getString(
    resource: AppStringResource,
    vararg formatArgs: Any,
): String = LocalizationRuntime.getString(resource, *formatArgs)
