package com.velord.usecase.event

import com.velord.model.ToastConfig

fun interface ShowToastUC : suspend (ToastConfig) -> Unit
