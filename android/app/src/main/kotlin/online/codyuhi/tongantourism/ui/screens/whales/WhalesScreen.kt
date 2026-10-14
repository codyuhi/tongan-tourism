package online.codyuhi.tongantourism.ui.screens.whales

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.domain.WhaleSeason
import online.codyuhi.tongantourism.ui.components.CedarButton
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun WhalesScreen(
    viewModel: WhalesViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val data = state.whaleData

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            OutlinedCard(
                shape = CedarShapes.medium,
                colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
                border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = data.heroImageRes),
                            contentDescription = "Humpback Whales in Tonga",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xCC161614))
                                    )
                                )
                        )

                        TagBadge(
                            text = "🐋 SANCTUARY EST. 1979",
                            backgroundColor = CedarPalette.Juniper,
                            textColor = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                        )

                        Text(
                            text = "Humpback Whale Sanctuary",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Every year from June to November, southern humpback whales migrate over 6,000 kilometers from freezing Antarctic waters to the sheltered, warm tropical lagoons of Tonga to mate and give birth.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Cedar.colors.textSecondary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Procedural Acoustic Whale Song Synthesizer Button
                        CedarButton(
                            text = if (state.isPlayingSound) "Playing Whale Acoustics..." else "Listen to Whale Song acoustics",
                            onClick = { viewModel.playWhaleSong() },
                            icon = "🎵",
                            isPrimary = !state.isPlayingSound,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Migration Season Calendar
        item {
            Column {
                Text(
                    text = "MIGRATION TIMELINE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Cedar.colors.brand,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Month-by-Month Calendar",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    data.season.forEach { season ->
                        SeasonMonthRow(season = season)
                    }
                }
            }
        }

        // Swimmer Rules Card
        item {
            OutlinedCard(
                shape = CedarShapes.medium,
                colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
                border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚖️ ETHICAL SWIMMER REGULATIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CedarPalette.Rust,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tonga strictly enforces licensed ecotourism laws to protect whale mothers and calves:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Cedar.colors.textSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    data.rules.forEach { rule ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Text(
                                text = "🛡️ ",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = rule,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Cedar.colors.textPrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Sanctuary Marine Facts
        item {
            OutlinedCard(
                shape = CedarShapes.medium,
                colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.inset),
                border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌊 SANCTUARY FACTS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    data.facts.forEach { fact ->
                        Text(
                            text = "• $fact",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Cedar.colors.textSecondary
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SeasonMonthRow(season: WhaleSeason) {
    val isPeak = season.status.contains("Peak")
    val badgeBg = if (isPeak) CedarPalette.Rust else Cedar.colors.inset
    val badgeText = if (isPeak) Color.White else Cedar.colors.textPrimary

    OutlinedCard(
        shape = CedarShapes.small,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, if (isPeak) CedarPalette.Rust.copy(alpha = 0.5f) else Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = season.month,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    TagBadge(
                        text = season.status,
                        backgroundColor = badgeBg,
                        textColor = badgeText
                    )
                }
                Text(
                    text = season.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.textSecondary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
