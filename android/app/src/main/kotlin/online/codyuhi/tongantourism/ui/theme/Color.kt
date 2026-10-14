package online.codyuhi.tongantourism.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CedarColors(
    val canvas: Color,          // --cdr-bg-canvas
    val surface: Color,         // cards, panels
    val inset: Color,           // tracks, segmented controls, metrics
    val hover: Color,           // pressed / selected rows, table headers
    val focal: Color,           // pure focal readouts only
    val brand: Color,           // Spruce: primary buttons, active tabs
    val brandAccent: Color,     // badges, status, dark-mode brand text
    val alert: Color,           // Rust: destructive, critical
    val textEmphasis: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderDistinct: Color,
    val successBg: Color, val successText: Color,
    val warningBg: Color, val warningText: Color,
    val errorBg: Color, val errorText: Color,
    val infoBg: Color, val infoText: Color,
)

val CedarLight = CedarColors(
    canvas = Color(0xFFEBE7DF),
    surface = Color(0xFFF5F3EC),
    inset = Color(0xFFE4DFD4),
    hover = Color(0xFFDBD5C7),
    focal = Color(0xFFFFFFFF),
    brand = Color(0xFF1F513F),
    brandAccent = Color(0xFF235C48),
    alert = Color(0xFFC7370F),
    textEmphasis = Color(0xFF141412),
    textPrimary = Color(0xFF242320),
    textSecondary = Color(0xFF48453D),
    textMuted = Color(0xFF666257),
    borderSubtle = Color(0xFFCDC7BA),
    borderDistinct = Color(0xFFABA496),
    successBg = Color(0xFFD8E8DC), successText = Color(0xFF1F513F),
    warningBg = Color(0xFFEDE4CE), warningText = Color(0xFF854714),
    errorBg = Color(0xFFEDDCD5), errorText = Color(0xFF811823),
    infoBg = Color(0xFFD6E4EE), infoText = Color(0xFF1B437E),
)

val CedarDark = CedarColors(
    canvas = Color(0xFF161614),
    surface = Color(0xFF22221F),
    inset = Color(0xFF1B1B18),
    hover = Color(0xFF2D2C28),
    focal = Color(0xFF2D2C28),
    brand = Color(0xFF235C48),
    brandAccent = Color(0xFF48A97C),
    alert = Color(0xFFE0532B),
    textEmphasis = Color(0xFFFFFFFF),
    textPrimary = Color(0xFFF4F2ED),
    textSecondary = Color(0xFFD5CFC3),
    textMuted = Color(0xFF9E998E),
    borderSubtle = Color(0xFF383630),
    borderDistinct = Color(0xFF545149),
    successBg = Color(0x382E6B34), successText = Color(0xFF4ADE80),
    warningBg = Color(0x38854714), warningText = Color(0xFFFBBF24),
    errorBg = Color(0x38811823), errorText = Color(0xFFF87171),
    infoBg = Color(0x381B437E), infoText = Color(0xFF60A5FA),
)

val LocalCedarColors = staticCompositionLocalOf { CedarLight }

fun CedarColors.toMaterial(dark: Boolean) = if (dark) {
    darkColorScheme(
        primary = brand, onPrimary = Color.White,
        secondary = brandAccent, onSecondary = canvas,
        tertiary = Color(0xFFB8860B),
        background = canvas, onBackground = textPrimary,
        surface = surface, onSurface = textPrimary,
        surfaceVariant = inset, onSurfaceVariant = textSecondary,
        surfaceContainer = surface, surfaceContainerHigh = hover, surfaceContainerLow = inset,
        outline = borderDistinct, outlineVariant = borderSubtle,
        error = alert, onError = Color.White,
    )
} else {
    lightColorScheme(
        primary = brand, onPrimary = Color.White,
        secondary = brandAccent, onSecondary = Color.White,
        tertiary = Color(0xFFB8860B),
        background = canvas, onBackground = textPrimary,
        surface = surface, onSurface = textPrimary,
        surfaceVariant = inset, onSurfaceVariant = textSecondary,
        surfaceContainer = surface, surfaceContainerHigh = hover, surfaceContainerLow = inset,
        outline = borderDistinct, outlineVariant = borderSubtle,
        error = alert, onError = Color.White,
    )
}
