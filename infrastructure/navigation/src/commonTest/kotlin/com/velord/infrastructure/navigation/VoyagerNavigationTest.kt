package com.velord.infrastructure.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.velord.core.navigation.voyager.SharedScreenVoyager
import com.velord.infrastructure.navigation.voyager.initVoyager
import com.velord.infrastructure.navigation.voyager.screen.CameraRecordingVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.DemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.DialogDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.FlowSummatorVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.HintPhoneNumberVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.ModifierDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.MorphDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.MovieVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.SettingsVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.ShapeDemoVoyagerScreen
import com.velord.infrastructure.navigation.voyager.screen.voyagerScreenProvider
import com.velord.ui.feature.demo.DemoNavigationEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class VoyagerNavigationTest {

    @Test
    fun `registry initialization is platform neutral and idempotent`() {
        // TODO: 2 init voyagers why ?
        initVoyager()
        initVoyager()

        assertSame(
            CameraRecordingVoyagerScreen,
            ScreenRegistry.get(SharedScreenVoyager.BottomNavigationTab.Camera),
        )
    }

    @Test
    fun `bottom navigation providers resolve to their screens`() {
        initVoyager()
        val registrationRoster = listOf(
            SharedScreenVoyager.BottomNavigationTab.Camera to CameraRecordingVoyagerScreen,
            SharedScreenVoyager.BottomNavigationTab.Demo to DemoVoyagerScreen,
            SharedScreenVoyager.BottomNavigationTab.Settings to SettingsVoyagerScreen,
        )

        registrationRoster.forEach { (provider, expectedScreen) ->
            assertSame(expectedScreen, ScreenRegistry.get(provider))
        }
    }

    @Test
    fun `every demo event maps to a unique registered Voyager screen`() {
        initVoyager()
        val expectedScreenRoster = listOf(
            ShapeDemoVoyagerScreen,
            ModifierDemoVoyagerScreen,
            FlowSummatorVoyagerScreen,
            MorphDemoVoyagerScreen,
            HintPhoneNumberVoyagerScreen,
            MovieVoyagerScreen,
            DialogDemoVoyagerScreen,
        )
        val providerRoster = DemoNavigationEvent.entries.map { event ->
            event.voyagerScreenProvider()
        }
        val screenRoster = providerRoster.map(ScreenRegistry::get)

        assertEquals(DemoNavigationEvent.entries.size, providerRoster.size)
        assertEquals(providerRoster.size, providerRoster.toSet().size)
        assertEquals(expectedScreenRoster, screenRoster)
    }
}
