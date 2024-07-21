package com.velord.composescreenexample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.velord.appstate.AppStateModule
import com.velord.backend.ktor.BackendModule
import com.velord.backend.ktor.httpModule
import com.velord.bd.BDModule
import com.velord.bottomnavigation.BottomNavigationModule
import com.velord.camerarecording.CameraRecordingViewModel
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.composescreenexample.ui.main.navigation.featureBottomNavigationModule
import com.velord.composescreenexample.ui.main.navigation.featureDemoModule
import com.velord.composescreenexample.ui.main.navigation.featureMainModule
import com.velord.datastore.DataStoreModule
import com.velord.feature.demo.DemoViewModel
import com.velord.feature.movie.viewModel.AllMovieViewModel
import com.velord.feature.movie.viewModel.FavoriteMovieViewModel
import com.velord.feature.movie.viewModel.MovieViewModel
import com.velord.flowsummator.FlowSummatorViewModel
import com.velord.gateway.GatewayModule
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.splash.SplashViewModel
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
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

private val useCaseModule = module {
    factory { GetThemeConfigUC(get()) }
    factory { SwitchThemeConfigUC(get()) }
    factory { SwitchAbideToOsThemeConfigUC(get()) }
    factory { SwitchDynamicColorThemeConfigUC(get()) }
    factory { GetAllMovieUC(get(), get()) }
    factory { GetFavoriteMovieUC(get()) }
    factory { GetMovieSortOptionUC(get()) }
    factory { SetMovieSortOptionUC(get()) }
    factory { UpdateMovieLikeUC(get()) }
    factory { LoadNewPageMovieUC(get()) }
    factory { RefreshMovieUC(get()) }
}

private val viewModelModule = module {
    // VieModel that can not be instantiated there has their own module DI
    viewModelOf(::ThemeViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::DemoViewModel)
    viewModelOf(::FlowSummatorViewModel)
    viewModelOf(::MovieViewModel)
    viewModelOf(::AllMovieViewModel)
    viewModelOf(::FavoriteMovieViewModel)
    viewModelOf(::CameraRecordingViewModel)
}

@Module
@ComponentScan("com.velord.composescreenexample")
class AppModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Navigation Voyager
        ScreenRegistry {
            (MainActivity.featureMainModule)()
            (MainActivity.featureBottomNavigationModule)()
            (MainActivity.featureDemoModule)()
        }

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(useCaseModule)
            modules(viewModelModule)
            modules(httpModule)
            modules(AppModule().module)
            modules(BottomNavigationModule().module)
            modules(DataStoreModule().module)
            modules(GatewayModule().module)
            modules(AppStateModule().module)
            modules(BackendModule().module)
            modules(BDModule().module)
        }

        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyFlashScreen()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
               // .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}