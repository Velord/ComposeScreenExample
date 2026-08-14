package com.velord.infrastructure.di

import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationVM
import com.velord.ui.feature.camerarecording.viewModel.CameraRecordingVM
import com.velord.ui.feature.demo.DemoVM
import com.velord.ui.feature.demo.dialog.DialogDemoVM
import com.velord.ui.feature.flowsummator.FlowSummatorVM
import com.velord.ui.feature.movie.viewModel.AllMovieVM
import com.velord.ui.feature.movie.viewModel.FavoriteMovieVM
import com.velord.ui.feature.movie.viewModel.MovieVM
import com.velord.ui.feature.splash.SplashVM
import com.velord.ui.sharedviewmodel.LanguageVM
import com.velord.ui.sharedviewmodel.MainVM
import com.velord.ui.sharedviewmodel.ThemeVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // VieModel that can not be \or have not to be instantiated here has their own module DI
    viewModel { ThemeVM(get(), get(), get(), get()) }
    viewModel { LanguageVM(get(), get()) }
    viewModel { SplashVM(get(), get()) }
    viewModel { DemoVM(get(), get()) }
    viewModel { FlowSummatorVM() }
    viewModel { MovieVM(get(), get(), get(), get()) }
    viewModel { AllMovieVM(get(), get(), get(), get(), get()) }
    viewModel { FavoriteMovieVM(get(), get()) }
    viewModel { CameraRecordingVM(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { BottomNavigationVM(get(), get(), get()) }
    viewModel { DialogDemoVM() }
    viewModel { MainVM(get(), get(), get()) }
}
