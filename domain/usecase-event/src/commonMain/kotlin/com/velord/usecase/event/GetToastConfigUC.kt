package com.velord.usecase.event

import com.velord.model.ToastConfig
import kotlinx.coroutines.flow.Flow

fun interface GetToastConfigUC : () -> Flow<ToastConfig>
