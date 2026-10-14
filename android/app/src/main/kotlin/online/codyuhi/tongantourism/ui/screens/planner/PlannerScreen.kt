package online.codyuhi.tongantourism.ui.screens.planner

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.domain.ItineraryDay
import online.codyuhi.tongantourism.domain.SavedItinerary
import online.codyuhi.tongantourism.ui.components.CedarButton
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun PlannerScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                TagBadge(
                    text = "🗺️ CUSTOM TRIP BUILDER",
                    backgroundColor = Cedar.colors.inset,
                    textColor = Cedar.colors.brand
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kingdom of Tonga Itinerary Planner",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Text(
                    text = "Customize trip duration, select target island groups, and pick your travel style to generate a tailored itinerary saved locally to your device.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Configuration Card
        item {
            OutlinedCard(
                shape = CedarShapes.medium,
                colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
                border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 1. Duration Selection
                    Text(
                        text = "1. TRIP DURATION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 5, 7, 10, 14).forEach { days ->
                            SelectablePill(
                                label = "$days Days",
                                isSelected = state.selectedDuration == days,
                                onClick = {
                                    viewModel.setDuration(days)
                                    viewModel.generate()
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Islands Selection
                    Text(
                        text = "2. DESTINATION ISLANDS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            "tongatapu" to "Tongatapu (Royal Seat & Megaliths)",
                            "vavau" to "Vava'u (Whales & Sailing)",
                            "haapai" to "Ha'apai (Coral Atolls & Solitude)",
                            "eua" to "'Eua (Rainforest & Caving)"
                        ).forEach { (id, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.toggleIsland(id)
                                        viewModel.generate()
                                    }
                                    .padding(vertical = 2.dp)
                            ) {
                                Checkbox(
                                    checked = state.selectedIslands.contains(id),
                                    onCheckedChange = {
                                        viewModel.toggleIsland(id)
                                        viewModel.generate()
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Cedar.colors.brand,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Cedar.colors.textPrimary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Travel Vibe
                    Text(
                        text = "3. TRAVEL STYLE & VIBE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Balanced Explorer",
                            "Whale & Ocean Adventure",
                            "Culture & Heritage",
                            "Relaxed Beach & Solitude"
                        ).forEach { vibe ->
                            SelectablePill(
                                label = vibe,
                                isSelected = state.selectedVibe == vibe,
                                onClick = {
                                    viewModel.setVibe(vibe)
                                    viewModel.generate()
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CedarButton(
                        text = "Generate New Itinerary",
                        onClick = { viewModel.generate() },
                        icon = "✨",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Generated Itinerary Results Header
        state.generatedItinerary?.let { itin ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GENERATED SCHEDULE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Cedar.colors.brand,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = itin.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Cedar.colors.textEmphasis
                            )
                        )
                    }
                }
            }

            // Days List
            items(itin.days, key = { it.day }) { day ->
                DayScheduleCard(day = day)
            }

            // Action row: Save to Room + Share via Sharesheet
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CedarButton(
                        text = if (state.isSavedSuccess) "Saved to Trips ✓" else "Save Itinerary",
                        onClick = { viewModel.saveCurrentItinerary() },
                        icon = "💾",
                        isPrimary = !state.isSavedSuccess,
                        modifier = Modifier.weight(1f)
                    )

                    CedarButton(
                        text = "Share Itinerary",
                        onClick = { shareItinerary(context, itin) },
                        icon = "📤",
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Saved Itineraries Section
        if (state.savedItineraries.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SAVED TRAVEL PLANS (${state.savedItineraries.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Cedar.colors.textMuted,
                        letterSpacing = 1.sp
                    )
                )
            }

            items(state.savedItineraries, key = { it.id }) { saved ->
                SavedTripCard(
                    itinerary = saved,
                    onDelete = { viewModel.deleteItinerary(saved.id) },
                    onShare = { shareItinerary(context, saved) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SelectablePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) Cedar.colors.brand else Cedar.colors.inset
    val textCol = if (isSelected) Color.White else Cedar.colors.textPrimary
    val borderCol = if (isSelected) Cedar.colors.brand else Cedar.colors.borderSubtle

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(BorderStroke(1.dp, borderCol), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textCol
            )
        )
    }
}

@Composable
private fun DayScheduleCard(day: ItineraryDay) {
    OutlinedCard(
        shape = CedarShapes.small,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Day badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Cedar.colors.brand),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "D${day.day}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                    TagBadge(
                        text = day.island,
                        backgroundColor = Cedar.colors.inset,
                        textColor = Cedar.colors.brand
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = day.desc,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    )
                )
            }
        }
    }
}

@Composable
private fun SavedTripCard(
    itinerary: SavedItinerary,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    OutlinedCard(
        shape = CedarShapes.small,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = itinerary.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    ),
                    modifier = Modifier.weight(1f)
                )
                TagBadge(
                    text = itinerary.duration,
                    backgroundColor = Cedar.colors.inset,
                    textColor = Cedar.colors.brand
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Islands: ${itinerary.islands.joinToString(", ")} • Style: ${itinerary.vibe}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Cedar.colors.textMuted
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Share 📤",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Cedar.colors.brand,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .clickable { onShare() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Delete ✕",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CedarPalette.Rust,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .clickable { onDelete() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun shareItinerary(context: Context, itinerary: SavedItinerary) {
    val text = buildString {
        appendLine("🌴 Kingdom of Tonga Itinerary: ${itinerary.title}")
        appendLine("Duration: ${itinerary.duration} | Style: ${itinerary.vibe}")
        appendLine("Islands: ${itinerary.islands.joinToString(", ")}")
        appendLine()
        itinerary.days.forEach { day ->
            appendLine("Day ${day.day} (${day.island}): ${day.title}")
            appendLine("${day.desc}")
            appendLine()
        }
        appendLine("Shared from Tonga Tourism Native App.")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, itinerary.title)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Itinerary via"))
}
