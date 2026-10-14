package online.codyuhi.tongantourism.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun CedarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    isPrimary: Boolean = true
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            shape = CedarShapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = Cedar.colors.brand,
                contentColor = Color.White
            ),
            modifier = modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Text(text = icon, modifier = Modifier.padding(end = 6.dp))
                }
                Text(
                    text = text,
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = CedarShapes.small,
            border = BorderStroke(1.dp, Cedar.colors.borderDistinct),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Cedar.colors.textPrimary
            ),
            modifier = modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Text(text = icon, modifier = Modifier.padding(end = 6.dp))
                }
                Text(
                    text = text,
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun StatPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Cedar.colors.inset)
            .border(BorderStroke(1.dp, Cedar.colors.borderSubtle), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$label: ",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.textMuted
                )
            )
            Text(
                text = value,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Cedar.colors.textPrimary
                )
            )
        }
    }
}

@Composable
fun TagBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Cedar.colors.inset,
    textColor: Color = Cedar.colors.textSecondary
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        )
    }
}

@Composable
fun OfflineBanner(
    modifier: Modifier = Modifier,
    message: String = "Offline Mode: Full guide & local itineraries active"
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Cedar.colors.warningBg)
            .border(BorderStroke(1.dp, Cedar.colors.borderSubtle), RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Cedar.colors.warningText)
            )
            Text(
                text = message,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall.copy(
                    color = Cedar.colors.warningText,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
