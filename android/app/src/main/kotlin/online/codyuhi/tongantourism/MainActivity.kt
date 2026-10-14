package online.codyuhi.tongantourism

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.launch
import online.codyuhi.tongantourism.ui.TonganTourismApp
import online.codyuhi.tongantourism.ui.navigation.NavDestination
import online.codyuhi.tongantourism.ui.theme.CedarTheme
import online.codyuhi.tongantourism.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        val container = (application as TonganTourismApplication).container
        var initialDest by mutableStateOf(resolveInitialDestination(intent))

        setContent {
            val themeMode by container.settingsRepository.themeMode.collectAsState(initial = ThemeMode.System)
            val scope = rememberCoroutineScope()

            val systemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                ThemeMode.System -> systemDark
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }

            LaunchedEffect(isDark) {
                val style = if (isDark) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
            }

            CedarTheme(mode = themeMode) {
                TonganTourismApp(
                    container = container,
                    currentThemeMode = themeMode,
                    onToggleTheme = {
                        val next = when (themeMode) {
                            ThemeMode.System -> ThemeMode.Light
                            ThemeMode.Light -> ThemeMode.Dark
                            ThemeMode.Dark -> ThemeMode.System
                        }
                        scope.launch {
                            container.settingsRepository.setThemeMode(next)
                        }
                    },
                    initialDestination = initialDest
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun resolveInitialDestination(intent: Intent?): NavDestination {
        val data = intent?.data ?: return NavDestination.Discover
        return when (data.host) {
            "planner" -> NavDestination.Planner
            "whales" -> NavDestination.Whales
            "phrases" -> NavDestination.Essentials
            else -> NavDestination.Discover
        }
    }
}
