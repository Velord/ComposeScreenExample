package com.velord.navigation.compose.nav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal interface GraphNav3 {

    sealed interface Main {

        @Serializable
        object BottomNavigationDestinationNav3 : NavKey

        @Serializable
        object SettingDestinationNav3 : NavKey
    }

    sealed interface BottomTab {

        sealed interface CameraRecording {

            // Graph is not supported in Nav3
//            @Serializable
//            object Self : NavKey


            @Serializable
            object CameraRecordingDestinationNav3 : NavKey
        }

        sealed interface Demo {

            // Graph is not supported in Nav3
//            @Serializable
//            object Self : NavKey

            @Serializable
            object DemoDestinationNav3 : NavKey

            @Serializable
            object FlowSummatorDestinationNav3 : NavKey

            @Serializable
            object HintPhoneDestinationNav3 : NavKey

            @Serializable
            object ModifierDestinationNav3 : NavKey

            @Serializable
            object MorphDemoDestinationNav3 : NavKey

            @Serializable
            object ShapeDemoDestinationNav3 : NavKey

            @Serializable
            object MovieDestinationNav3 : NavKey

            @Serializable
            object DialogDestinationNav3 : NavKey
        }

        @Serializable
        object SettingDestinationNav3 : NavKey
    }
}