package online.codyuhi.tongantourism.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.ThemeMode

@Composable
fun CedarThemeToggle(
    currentMode: ThemeMode,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (currentMode) {
        ThemeMode.Dark -> "☀️"
        ThemeMode.Light -> "🌙"
        ThemeMode.System -> "🌓"
    }

    val desc = when (currentMode) {
        ThemeMode.Dark -> "Switch to light theme"
        ThemeMode.Light -> "Switch to dark theme"
        ThemeMode.System -> "Switch theme (currently following system)"
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Cedar.colors.surface)
            .border(BorderStroke(1.dp, Cedar.colors.borderDistinct), RoundedCornerShape(6.dp))
            .clickable(role = Role.Button, onClick = onToggle)
            .semantics {
                this.contentDescription = desc
                this.role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
    }
}
