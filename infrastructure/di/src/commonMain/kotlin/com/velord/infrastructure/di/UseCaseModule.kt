package com.velord.infrastructure.di

import com.velord.data.gateway.camera.CameraRecordingGateway
import com.velord.data.gateway.camera.CameraSessionGateway
import com.velord.data.gateway.camera.CameraStateGateway
import com.velord.data.gateway.event.AppEventGateway
import com.velord.data.gateway.file.FileGateway
import com.velord.data.gateway.localization.LocalizationGateway
import com.velord.data.gateway.movie.MovieByGateway
import com.velord.data.gateway.movie.MovieFavoriteGateway
import com.velord.data.gateway.movie.MovieFilterGateway
import com.velord.data.gateway.movie.MovieGateway
import com.velord.data.gateway.movie.MoviePaginationGateway
import com.velord.data.gateway.movie.MovieSortGateway
import com.velord.data.gateway.setting.GetThemeConfigGateway
import com.velord.data.gateway.setting.SwitchThemeConfigGateway
import com.velord.usecase.camera.CreateCameraSessionUC
import com.velord.usecase.camera.GetCameraSessionUC
import com.velord.usecase.camera.GetCameraStateUC
import com.velord.usecase.camera.GetLastCameraVideoAssetUC
import com.velord.usecase.camera.OpenCameraVideoFolderUC
import com.velord.usecase.camera.PauseRecordingUC
import com.velord.usecase.camera.ReleaseCameraSessionUC
import com.velord.usecase.camera.ResumeRecordingUC
import com.velord.usecase.camera.StartRecordingUC
import com.velord.usecase.camera.StopRecordingUC
import com.velord.usecase.camera.ToggleCameraLensUC
import com.velord.usecase.event.GetAppEventFlowUC
import com.velord.usecase.event.GetToastConfigFlowUC
import com.velord.usecase.event.RequestAppExitUC
import com.velord.usecase.event.ShowToastUC
import com.velord.usecase.movie.GetAllMovieUC
import com.velord.usecase.movie.GetFavoriteMovieUC
import com.velord.usecase.movie.GetMovieFilterOptionUC
import com.velord.usecase.movie.GetMovieSortOptionUC
import com.velord.usecase.movie.LoadNewPageMovieUC
import com.velord.usecase.movie.RefreshMovieUC
import com.velord.usecase.movie.SetMovieFilterOptionUC
import com.velord.usecase.movie.SetMovieSortOptionUC
import com.velord.usecase.movie.ShareMovieUC
import com.velord.usecase.movie.UpdateMovieLikeUC
import com.velord.usecase.setting.GetLanguagePreferenceUC
import com.velord.usecase.setting.GetLocalizationStateUC
import com.velord.usecase.setting.GetThemeConfigUC
import com.velord.usecase.setting.InitializeLocalizationUC
import com.velord.usecase.setting.SetLanguagePreferenceUC
import com.velord.usecase.setting.SwitchAbideToOsThemeConfigUC
import com.velord.usecase.setting.SwitchDarkThemeConfigUC
import com.velord.usecase.setting.SwitchDynamicColorThemeConfigUC
import com.velord.usecase.setting.SwitchShapeStyleThemeConfigUC
import com.velord.usecase.setting.SwitchSpecialThemeConfigUC
import org.koin.dsl.module

val useCaseModule = module {
    single<GetToastConfigFlowUC> {
        GetToastConfigFlowUC(get<AppEventGateway>()::getToastFlow)
    }
    single<GetAppEventFlowUC> {
        GetAppEventFlowUC(get<AppEventGateway>()::getFlow)
    }
    single<ShowToastUC> {
        ShowToastUC(get<AppEventGateway>()::showToast)
    }
    single<RequestAppExitUC> {
        RequestAppExitUC(get<AppEventGateway>()::requestExit)
    }
    single<GetThemeConfigUC> {
        GetThemeConfigUC(get<GetThemeConfigGateway>()::getFlow)
    }
    single<SwitchDarkThemeConfigUC> {
        SwitchDarkThemeConfigUC(get<SwitchThemeConfigGateway>()::switchDarkTheme)
    }
    single<SwitchSpecialThemeConfigUC> {
        SwitchSpecialThemeConfigUC(get<SwitchThemeConfigGateway>()::switchSpecialTheme)
    }
    single<SwitchShapeStyleThemeConfigUC> {
        SwitchShapeStyleThemeConfigUC(get<SwitchThemeConfigGateway>()::switchShapeStyle)
    }
    single<SwitchAbideToOsThemeConfigUC> {
        SwitchAbideToOsThemeConfigUC(get<SwitchThemeConfigGateway>()::switchAbideToOs)
    }
    single<SwitchDynamicColorThemeConfigUC> {
        SwitchDynamicColorThemeConfigUC(get<SwitchThemeConfigGateway>()::switchDynamicColor)
    }
    single<InitializeLocalizationUC> {
        InitializeLocalizationUC(get<LocalizationGateway>()::initialize)
    }
    single<GetLocalizationStateUC> {
        GetLocalizationStateUC(get<LocalizationGateway>()::getStateFlow)
    }
    single<GetLanguagePreferenceUC> {
        GetLanguagePreferenceUC(get<LocalizationGateway>()::getLanguagePreferenceFlow)
    }
    single<SetLanguagePreferenceUC> {
        SetLanguagePreferenceUC(get<LocalizationGateway>()::setLanguagePreference)
    }
    single<GetAllMovieUC> {
        GetAllMovieUC(get<MovieByGateway>()::getBySortAndFilter)
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
    single<GetMovieFilterOptionUC> {
        GetMovieFilterOptionUC(get<MovieFilterGateway>()::getFlow)
    }
    single<SetMovieFilterOptionUC> {
        SetMovieFilterOptionUC(get<MovieFilterGateway>()::update)
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
    single<ShareMovieUC> {
        ShareMovieUC(get<MovieGateway>()::share)
    }
    single<StartRecordingUC> {
        StartRecordingUC(get<CameraRecordingGateway>()::startRecording)
    }
    single<StopRecordingUC> {
        StopRecordingUC(get<CameraRecordingGateway>()::stopRecording)
    }
    single<PauseRecordingUC> {
        PauseRecordingUC(get<CameraRecordingGateway>()::pauseRecording)
    }
    single<ResumeRecordingUC> {
        ResumeRecordingUC(get<CameraRecordingGateway>()::resumeRecording)
    }
    single<ToggleCameraLensUC> {
        ToggleCameraLensUC(get<CameraRecordingGateway>()::toggleCameraLens)
    }
    single<CreateCameraSessionUC> {
        CreateCameraSessionUC(get<CameraSessionGateway>()::createSession)
    }
    single<ReleaseCameraSessionUC> {
        ReleaseCameraSessionUC(get<CameraSessionGateway>()::releaseSession)
    }
    single<GetCameraSessionUC> {
        GetCameraSessionUC(get<CameraSessionGateway>()::getSession)
    }
    single<GetCameraStateUC> {
        GetCameraStateUC(get<CameraStateGateway>()::getState)
    }
    single<GetLastCameraVideoAssetUC> {
        GetLastCameraVideoAssetUC(get<CameraStateGateway>()::getLastVideoAsset)
    }
    single<OpenCameraVideoFolderUC> {
        OpenCameraVideoFolderUC(get<FileGateway>()::openDirectory)
    }
}

