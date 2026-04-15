package com.velord.di

import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.GetAllMovieUCImpl
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.GetFavoriteMovieUCImpl
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.GetMovieSortOptionUCImpl
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.LoadNewPageMovieUCImpl
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.RefreshMovieUCImpl
import com.velord.usecase.movie.SetMovieSortOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUCImpl
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.movie.UpdateMovieLikeUCImpl
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.GetThemeConfigUCImpl
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUCImpl
import com.velord.usecase.setting.SwitchDarkThemeConfigUC
import com.velord.usecase.setting.SwitchDarkThemeConfigUCImpl
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUCImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetThemeConfigUCImpl) bind GetThemeConfigUC::class
    singleOf(::SwitchDarkThemeConfigUCImpl) bind SwitchDarkThemeConfigUC::class
    singleOf(::SwitchAbideToOsThemeConfigUCImpl) bind SwitchAbideToOsThemeConfigUC::class
    singleOf(::SwitchDynamicColorThemeConfigUCImpl) bind SwitchDynamicColorThemeConfigUC::class
    singleOf(::GetAllMovieUCImpl) bind GetAllMovieUC::class
    singleOf(::GetFavoriteMovieUCImpl) bind GetFavoriteMovieUC::class
    singleOf(::GetMovieSortOptionUCImpl) bind GetMovieSortOptionUC::class
    singleOf(::SetMovieSortOptionUCImpl) bind SetMovieSortOptionUC::class
    singleOf(::UpdateMovieLikeUCImpl) bind UpdateMovieLikeUC::class
    singleOf(::LoadNewPageMovieUCImpl) bind LoadNewPageMovieUC::class
    singleOf(::RefreshMovieUCImpl) bind RefreshMovieUC::class
}
