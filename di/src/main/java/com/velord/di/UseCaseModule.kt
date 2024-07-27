package com.velord.di

import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import com.velord.usecase.setting.SwitchThemeConfigUC
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetThemeConfigUC(get()) }
    factory { SwitchThemeConfigUC(get()) }
    factory { SwitchAbideToOsThemeConfigUC(get()) }
    factory { SwitchDynamicColorThemeConfigUC(get()) }
    factory { GetAllMovieUC(get(), get()) }
    factory { GetFavoriteMovieUC(get(), get()) }
    factory { GetMovieSortOptionUC(get()) }
    factory { SetMovieSortOptionUC(get()) }
    factory { UpdateMovieLikeUC(get()) }
    factory { LoadNewPageMovieUC(get()) }
    factory { RefreshMovieUC(get()) }
}