package com.velord.infrastructure.di

import com.velord.ui.feature.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.ui.feature.camerarecording.CameraRecordingViewModel
import com.velord.ui.feature.demo.dialog.DialogDemoViewModel
import com.velord.ui.feature.demo.DemoViewModel
import com.velord.ui.feature.movie.viewModel.AllMovieViewModel
import com.velord.ui.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.ui.feature.movie.viewModel.MovieViewModel
import com.velord.ui.feature.flowsummator.FlowSummatorViewModel
import com.velord.ui.sharedviewmodel.ThemeViewModel
import com.velord.ui.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // VieModel that can not be instantiated here has their own module DI
    viewModel { ThemeViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel() }
    viewModel { DemoViewModel(get(), get()) }
    viewModel { FlowSummatorViewModel() }
    viewModel { MovieViewModel(get(), get()) }
    viewModel { AllMovieViewModel(get(), get(), get(), get()) }
    viewModel { FavoriteMovieViewModel(get(), get()) }
    viewModel { CameraRecordingViewModel(get()) }
    viewModel { BottomNavigationJetpackVM(get()) }
    viewModel { DialogDemoViewModel() }
}