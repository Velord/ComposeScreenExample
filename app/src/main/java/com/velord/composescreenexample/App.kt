package com.velord.composescreenexample

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.velord.bottomnavigation.BottomNavModule
import com.velord.camerarecording.CameraRecordingModule
import com.velord.composescreenexample.ui.main.MainActivity
import com.velord.datastore.DataStoreModule
import com.velord.sharedviewmodel.SharedViewModelModule
import com.velord.usecase.setting.SettingUseCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

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
            modules(SharedViewModelModule().module)
            modules(DataStoreModule().module)
            modules(SettingUseCaseModule().module)
        }
    }
}