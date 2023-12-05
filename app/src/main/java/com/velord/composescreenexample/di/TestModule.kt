package com.velord.composescreenexample.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

class ValidateUserNameUseCase {
    init {
        Log.d("!!!", "ValidateUserNameUseCase init: ${this}")
    }

    fun inv() {}
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object TestModule {

    @ActivityRetainedScoped
    @Provides
    fun provideValidateUsernameUseCase(): ValidateUserNameUseCase = ValidateUserNameUseCase()
}