package com.velord.navigation.compose.vanilla

import kotlinx.serialization.Serializable

internal interface GraphVanilla {

    sealed interface Main {

        @Serializable
        object BottomNavigationDestinationVanilla

        @Serializable
        object SettingDestinationVanilla
    }

    sealed interface BottomTab {

        sealed interface CameraRecording {

            @Serializable
            object Self


            @Serializable
            object CameraRecordingDestinationVanilla
        }

        sealed interface Demo {

            @Serializable
            object Self

            @Serializable
            object DemoDestinationVanilla

            @Serializable
            object FlowSummatorDestinationVanilla

            @Serializable
            object HintPhoneDestinationVanilla

            @Serializable
            object ModifierDestinationVanilla

            @Serializable
            object MorphDemoDestinationVanilla

            @Serializable
            object ShapeDemoDestinationVanilla

            @Serializable
            object MovieDestinationVanilla

            @Serializable
            object DialogDestinationVanilla
        }

        @Serializable
        object SettingDestinationVanilla
    }
}