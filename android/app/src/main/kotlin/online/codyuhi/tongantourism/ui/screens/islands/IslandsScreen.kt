package online.codyuhi.tongantourism.ui.screens.islands

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import online.codyuhi.tongantourism.domain.Highlight
import online.codyuhi.tongantourism.domain.Island
import online.codyuhi.tongantourism.ui.components.CedarButton
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslandsScreen(
    viewModel: IslandsViewModel,
    onPlanTripToIsland: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val isWideScreen = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val columnsCount = if (isWideScreen) 2 else 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                label = "All Archipelagos",
                isSelected = state.selectedFilter == "all",
                onClick = { viewModel.setFilter("all") }
            )
            FilterChip(
                label = "Tongatapu",
                isSelected = state.selectedFilter == "tongatapu",
                onClick = { viewModel.setFilter("tongatapu") }
            )
            FilterChip(
                label = "Vava'u",
                isSelected = state.selectedFilter == "vavau",
                onClick = { viewModel.setFilter("vavau") }
            )
            FilterChip(
                label = "Ha'apai",
                isSelected = state.selectedFilter == "haapai",
                onClick = { viewModel.setFilter("haapai") }
            )
            FilterChip(
                label = "'Eua",
                isSelected = state.selectedFilter == "eua",
                onClick = { viewModel.setFilter("eua") }
            )
        }

        // Adaptive Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnsCount),
            contentPadding = PaddingValues(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.islands, key = { it.id }) { island ->
                IslandCard(
                    island = island,
                    onViewDetails = { viewModel.openIslandDetail(island) },
                    onPlanTrip = { onPlanTripToIsland(island.id) }
                )
            }
        }
    }

    // Detail Bottom Sheet
    state.selectedIslandDetail?.let { island ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeIslandDetail() },
            sheetState = sheetState,
            containerColor = Cedar.colors.surface,
            shape = CedarShapes.large
        ) {
            IslandDetailSheetContent(
                island = island,
                onPlanTrip = {
                    viewModel.closeIslandDetail()
                    onPlanTripToIsland(island.id)
                }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) Cedar.colors.brand else Cedar.colors.surface
    val textColor = if (isSelected) Color.White else Cedar.colors.textPrimary
    val border = if (isSelected) Cedar.colors.brand else Cedar.colors.borderDistinct

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        )
    }
}

@Composable
private fun IslandCard(
    island: Island,
    onViewDetails: () -> Unit,
    onPlanTrip: () -> Unit
) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = island.imageRes),
                    contentDescription = island.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
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

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = island.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )

                Text(
                    text = island.tagline,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.brand,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = island.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Highlights Summary
                Text(
                    text = "KEY HIGHLIGHTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Cedar.colors.textMuted,
                        letterSpacing = 1.sp
                    )
                )

                Column(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    island.highlights.take(2).forEach { hl ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "• ",
                                color = Cedar.colors.brand,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = hl.title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Cedar.colors.textPrimary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CedarButton(
                        text = "Full Guide",
                        onClick = onViewDetails,
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    CedarButton(
                        text = "Plan Trip",
                        onClick = onPlanTrip,
                        icon = "✈️",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun IslandDetailSheetContent(
    island: Island,
    onPlanTrip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = island.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Cedar.colors.textEmphasis
            )
        )

        Text(
            text = island.tagline,
            style = MaterialTheme.typography.titleSmall.copy(
                color = Cedar.colors.brand
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dual photo presentation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = island.imageRes),
                contentDescription = "${island.name} primary",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
            )
            Image(
                painter = painterResource(id = island.secondaryImageRes),
                contentDescription = "${island.name} secondary",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logistics Info
        OutlinedCard(
            shape = CedarShapes.small,
            colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.inset),
            border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "✈️ Airport: ${island.airport}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Text(
                    text = "⏱️ Transit from Capital: ${island.flightTimeFromCapital}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.textSecondary
                    )
                )
                Text(
                    text = "🏛️ Regional Capital: ${island.capital}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.textSecondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "LANDMARKS & HIGHLIGHTS",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Cedar.colors.textMuted,
                letterSpacing = 1.sp
            )
        )

        Column(
            modifier = Modifier.padding(top = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            island.highlights.forEach { hl ->
                Column {
                    Text(
                        text = "• ${hl.title}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                    Text(
                        text = hl.desc,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Cedar.colors.textSecondary
                        ),
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CedarButton(
            text = "Plan Trip to ${island.name}",
            onClick = onPlanTrip,
            icon = "🗺️",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
