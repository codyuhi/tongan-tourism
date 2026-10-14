package online.codyuhi.tongantourism.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    System,
    Light,
    Dark
}

@Composable
fun CedarTheme(
    mode: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit
) {
    val dark = when (mode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val cedar = if (dark) CedarDark else CedarLight
    CompositionLocalProvider(LocalCedarColors provides cedar) {
        MaterialTheme(
            colorScheme = cedar.toMaterial(dark).copy(surfaceTint = Color.Transparent),
            typography = CedarTypography,
            shapes = CedarShapes,
            content = content,
        )
    }
}

object Cedar {
    val colors: CedarColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCedarColors.current
}
