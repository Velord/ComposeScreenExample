package com.velord.di

import com.velord.gateway.movie.MovieByGateway
import com.velord.gateway.movie.MovieFavoriteGateway
import com.velord.gateway.movie.MoviePaginationGateway
import com.velord.gateway.movie.MovieSortGateway
import com.velord.gateway.setting.GetThemeConfigGateway
import com.velord.gateway.setting.SwitchThemeConfigGateway
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDarkThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import org.koin.dsl.module

val useCaseModule = module {
    single<GetThemeConfigUC> {
        GetThemeConfigUC(get<GetThemeConfigGateway>()::getFlow)
    }
    single<SwitchDarkThemeConfigUC> {
        SwitchDarkThemeConfigUC(get<SwitchThemeConfigGateway>()::switchDarkTheme)
    }
    single<SwitchAbideToOsThemeConfigUC> {
        SwitchAbideToOsThemeConfigUC(get<SwitchThemeConfigGateway>()::switchAbideToOs)
    }
    single<SwitchDynamicColorThemeConfigUC> {
        SwitchDynamicColorThemeConfigUC(get<SwitchThemeConfigGateway>()::switchDynamicColor)
    }
    single<GetAllMovieUC> {
        GetAllMovieUC(get<MovieByGateway>()::getBySort)
    }
    single<GetFavoriteMovieUC> {
        GetFavoriteMovieUC(get<MovieByGateway>()::getByFavorite)
    }
    single<GetMovieSortOptionUC> {
        GetMovieSortOptionUC(get<MovieSortGateway>()::getFlow)
    }
    single<SetMovieSortOptionUC> {
        SetMovieSortOptionUC(get<MovieSortGateway>()::update)
    }
    single<UpdateMovieLikeUC> {
        UpdateMovieLikeUC(get<MovieFavoriteGateway>()::update)
    }
    single<LoadNewPageMovieUC> {
        LoadNewPageMovieUC(get<MoviePaginationGateway>()::load)
    }
    single<RefreshMovieUC> {
        RefreshMovieUC(get<MoviePaginationGateway>()::refresh)
    }
}
