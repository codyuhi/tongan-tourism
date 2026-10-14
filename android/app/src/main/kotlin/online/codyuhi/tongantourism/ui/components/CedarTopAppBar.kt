package online.codyuhi.tongantourism.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.ThemeMode

@Composable
fun CedarTopAppBar(
    themeMode: ThemeMode,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
    savedTripsCount: Int = 0,
    onSavedTripsClick: (() -> Unit)? = null
) {
    Surface(
        color = Cedar.colors.canvas,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👑",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "Kingdom of Tonga",
                            style = Cedar.colors.let {
                                androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Cedar.colors.textEmphasis
                                )
                            }
                        )
                        Text(
                            text = "South Pacific Realm",
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall.copy(
                                color = Cedar.colors.textMuted
                            )
                        )
                    }
                }

                // Actions: Saved Trips Badge + Theme Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (savedTripsCount > 0 && onSavedTripsClick != null) {
                        Row(
                            modifier = Modifier
                                .height(38.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Cedar.colors.inset)
                                .border(1.dp, Cedar.colors.borderSubtle, RoundedCornerShape(6.dp))
                                .clickable { onSavedTripsClick() }
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✈️ $savedTripsCount",
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Cedar.colors.brand
                                )
                            )
                        }
                    }

                    CedarThemeToggle(
                        currentMode = themeMode,
                        onToggle = onToggleTheme
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            BoxDivider()
        }
    }
}

@Composable
private fun BoxDivider() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Cedar.colors.borderSubtle)
    )
}
