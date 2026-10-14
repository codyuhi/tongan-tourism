package online.codyuhi.tongantourism.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import online.codyuhi.tongantourism.R
import online.codyuhi.tongantourism.domain.Island
import online.codyuhi.tongantourism.ui.components.CedarButton
import online.codyuhi.tongantourism.ui.components.StatPill
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onNavigateToIslands: () -> Unit,
    onNavigateToWhales: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onSelectIsland: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Section
        item {
            HeroCard(
                onExploreClick = onNavigateToIslands,
                onWhalesClick = onNavigateToWhales
            )
        }

        // Live Info Bar (Tonga Clock, Weather, Currency)
        item {
            LiveInfoBar(
                tongaTime = state.tongaTime,
                weatherTemp = state.weather?.let { "${it.tempC}°C / ${it.tempF}°F" } ?: "26°C / 79°F",
                weatherDesc = state.weather?.condition ?: "Nuku'alofa Live",
                exchangeRate = "1 TOP = $${String.format(java.util.Locale.US, "%.2f", state.topUsdRate)} USD"
            )
        }

        // Kingdom Stats Grid
        item {
            KingdomStatsSection(stats = state.stats)
        }

        // Island Archipelagos Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "THE ARCHIPELAGOS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.brand,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Four Island Realms",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                }

                Text(
                    text = "See all ➔",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Cedar.colors.brand,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.clickable { onNavigateToIslands() }
                )
            }
        }

        // Island Cards Preview
        items(state.islands.size) { index ->
            val island = state.islands[index]
            IslandPreviewCard(
                island = island,
                onClick = { onSelectIsland(island.id) }
            )
        }

        // Itinerary Planner Banner Call-to-Action
        item {
            PlannerCtaCard(onPlanClick = onNavigateToPlanner)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeroCard(
    onExploreClick: () -> Unit,
    onWhalesClick: () -> Unit
) {
    Card(
        shape = CedarShapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_lagoon),
                contentDescription = "Kingdom of Tonga Turquoise Lagoon",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC161614),
                                Color(0xF2161614)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                TagBadge(
                    text = "🌴 South Pacific Realm",
                    backgroundColor = CedarPalette.Spruce,
                    textColor = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Unspoiled Kingdom of Tonga",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Swim with wild humpback whales, explore 170 tropical coral atolls, and embrace 3,000 years of Polynesian warmth.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFD5CFC3)
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CedarButton(
                        text = "Explore Islands",
                        onClick = onExploreClick,
                        modifier = Modifier.weight(1f)
                    )

                    CedarButton(
                        text = "Whale Guide",
                        onClick = onWhalesClick,
                        icon = "🐋",
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveInfoBar(
    tongaTime: String,
    weatherTemp: String,
    weatherDesc: String,
    exchangeRate: String
) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "REAL-TIME DESTINATION METRICS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.textMuted,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tonga Clock
                Column {
                    Text(
                        text = "🕐 Tonga Time (UTC+13)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Cedar.colors.textMuted)
                    )
                    Text(
                        text = tongaTime,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                }

                // Weather
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "☀️ Live Weather",
                        style = MaterialTheme.typography.labelSmall.copy(color = Cedar.colors.textMuted)
                    )
                    Text(
                        text = weatherTemp,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Currency Rate full width inside
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Cedar.colors.inset)
                    .border(BorderStroke(1.dp, Cedar.colors.borderSubtle), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💱 Market Exchange Rate",
                    style = MaterialTheme.typography.labelSmall.copy(color = Cedar.colors.textMuted)
                )
                Text(
                    text = exchangeRate,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.brand
                    )
                )
            }
        }
    }
}

@Composable
private fun KingdomStatsSection(stats: online.codyuhi.tongantourism.domain.KingdomStats) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatPill(
                label = "Total Islands",
                value = "${stats.islandsCount}",
                modifier = Modifier.weight(1f)
            )
            StatPill(
                label = "Inhabited",
                value = "${stats.inhabitedCount}",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatPill(
                label = "Whales Annually",
                value = stats.whalesAnnually,
                modifier = Modifier.weight(1f)
            )
            StatPill(
                label = "Heritage",
                value = stats.historyYears,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun IslandPreviewCard(
    island: Island,
    onClick: () -> Unit
) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = island.imageRes),
                    contentDescription = island.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x99161614))
                            )
                        )
                )

                TagBadge(
                    text = island.group,
                    backgroundColor = Cedar.colors.surface.copy(alpha = 0.9f),
                    textColor = Cedar.colors.textEmphasis,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = island.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )

                Text(
                    text = island.tagline,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.brand,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = island.description,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✈️ ${island.flightTimeFromCapital}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted
                        )
                    )

                    Text(
                        text = "View Guide ➔",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Cedar.colors.brand,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PlannerCtaCard(onPlanClick: () -> Unit) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.inset),
        border = androidx.compose.foundation.BorderStroke(1.dp, Cedar.colors.borderDistinct),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🗺️ TRIP SCHEDULE GENERATOR",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.brand,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Craft Your Custom Tongan Itinerary",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Cedar.colors.textEmphasis
                )
            )

            Text(
                text = "Select your trip duration, islands, and travel style to generate and save an offline schedule.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Cedar.colors.textSecondary
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            CedarButton(
                text = "Open Itinerary Planner ➔",
                onClick = onPlanClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
