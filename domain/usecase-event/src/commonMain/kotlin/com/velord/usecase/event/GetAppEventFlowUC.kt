package com.velord.usecase.event

import com.velord.model.AppEvent
import kotlinx.coroutines.flow.Flow

fun interface GetAppEventFlowUC : () -> Flow<AppEvent>
