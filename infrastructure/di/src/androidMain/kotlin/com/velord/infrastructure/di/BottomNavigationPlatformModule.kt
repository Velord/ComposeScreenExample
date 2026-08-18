package com.velord.infrastructure.di

import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal actual val bottomNavigationPlatformModule = module {
    viewModel { BottomNavigationJetpackVM(get(), get(), get(), get()) }
}
