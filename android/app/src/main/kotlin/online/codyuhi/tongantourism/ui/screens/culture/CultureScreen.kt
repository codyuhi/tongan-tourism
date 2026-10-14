package online.codyuhi.tongantourism.ui.screens.culture

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.domain.CultureItem
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun CultureScreen(
    viewModel: CultureViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                TagBadge(
                    text = "🌺 3,000 YEARS OF HERITAGE",
                    backgroundColor = Cedar.colors.inset,
                    textColor = Cedar.colors.brand
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Polynesian Traditions & Protocol",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Text(
                    text = "Tonga is the only Pacific nation never colonized by a foreign power, maintaining unbroken royal ceremonies, sacred hospitality, and ancient artisan traditions.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        items(state.items, key = { it.id }) { item ->
            CultureCard(item = item)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CultureCard(item: CultureItem) {
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
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                TagBadge(
                    text = "FAKA-TONGA",
                    backgroundColor = Cedar.colors.surface.copy(alpha = 0.9f),
                    textColor = Cedar.colors.brand,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )

                Text(
                    text = item.tagline,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.brand,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.summary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    )
                )

                // Details List
                if (item.details.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "SIGNIFICANCE & TRADITION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Column(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item.details.forEach { detail ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "• ",
                                    color = Cedar.colors.brand,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = detail,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Cedar.colors.textPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                // Etiquette Tips
                if (item.etiquetteTips.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedCard(
                        shape = CedarShapes.small,
                        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.inset),
                        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🤝 VISITOR ETIQUETTE & RESPECT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CedarPalette.Rust,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            item.etiquetteTips.forEach { tip ->
                                Text(
                                    text = "✓ $tip",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Cedar.colors.textPrimary
                                    ),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Dishes List (for cuisine)
                if (item.dishes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "TRADITIONAL SPECIALTIES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Column(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item.dishes.forEach { dish ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Cedar.colors.inset)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = dish.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Cedar.colors.brand
                                    )
                                )
                                Text(
                                    text = dish.desc,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Cedar.colors.textSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
