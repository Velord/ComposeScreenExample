package com.velord.di

import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.feature.demo.DemoViewModel
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieViewModel
import com.velord.flowsummator.FlowSummatorViewModel
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // VieModel that can not be instantiated here has their own module DI
    viewModel { ThemeViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel() }
    viewModel { DemoViewModel() }
    viewModel { FlowSummatorViewModel() }
    viewModel { MovieViewModel(get(), get()) }
    viewModel { AllMovieViewModel(get(), get(), get(), get(), get()) }
    viewModel { FavoriteMovieViewModel(get(), get()) }
    viewModel { CameraRecordingViewModel(get()) }
}