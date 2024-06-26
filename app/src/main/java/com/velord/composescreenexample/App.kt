package com.velord.composescreenexample

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.velord.appstate.AppStateModule
import com.velord.bottomnavigation.BottomNavModule
import com.velord.camerarecording.CameraRecordingModule
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.datastore.DataStoreModule
import com.velord.gateway.setting.SettingGatewayModule
import com.velord.sharedviewmodel.ThemeViewModel
import com.velord.usecase.setting.GetThemeConfigUC
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

private val useCaseModule = module {
    factory<GetThemeConfigUC> { GetThemeConfigUC(get()) }
}

private val viewModelModule = module {
    viewModelOf(::ThemeViewModel)
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

            modules(AppModule().module)
            modules(BottomNavModule().module)
            modules(CameraRecordingModule().module)
            modules(DataStoreModule().module)
            modules(SettingGatewayModule().module)
            modules(AppStateModule().module)
            modules(useCaseModule)
            modules(viewModelModule)
        }
    }
}