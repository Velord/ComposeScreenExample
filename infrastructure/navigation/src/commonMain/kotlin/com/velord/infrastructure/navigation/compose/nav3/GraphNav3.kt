package com.velord.infrastructure.navigation.compose.nav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface GraphNav3 : NavKey {

    @Serializable
    sealed interface Main : GraphNav3 {

        @Serializable
        object BottomNavigationDestinationNav3 : Main

        @Serializable
        object SettingDestinationNav3 : Main
    }

    @Serializable
    sealed interface BottomTab : GraphNav3 {

        @Serializable
        sealed interface CameraRecording : BottomTab {

            // Graph is not supported in Nav3
//            @Serializable
//            object Self : NavKey

            @Serializable
            object CameraRecordingDestinationNav3 : CameraRecording
        }

        @Serializable
        sealed interface Demo : BottomTab {

            // Graph is not supported in Nav3
//            @Serializable
//            object Self : NavKey

            @Serializable
            object DemoDestinationNav3 : Demo

            @Serializable
            object FlowSummatorDestinationNav3 : Demo

            @Serializable
            object HintPhoneDestinationNav3 : Demo

            @Serializable
            object ModifierDestinationNav3 : Demo

            @Serializable
            object MorphDemoDestinationNav3 : Demo

            @Serializable
            object ShapeDemoDestinationNav3 : Demo

            @Serializable
            object MovieDestinationNav3 : Demo

            @Serializable
            object DialogDestinationNav3 : Demo
        }

        @Serializable
        object SettingDestinationNav3 : BottomTab
    }
}
