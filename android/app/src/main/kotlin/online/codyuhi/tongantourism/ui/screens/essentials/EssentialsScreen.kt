package online.codyuhi.tongantourism.ui.screens.essentials

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.codyuhi.tongantourism.domain.Phrase
import online.codyuhi.tongantourism.ui.components.CedarButton
import online.codyuhi.tongantourism.ui.components.TagBadge
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.CedarMonoStyle
import online.codyuhi.tongantourism.ui.theme.CedarPalette
import online.codyuhi.tongantourism.ui.theme.CedarShapes

@Composable
fun EssentialsScreen(
    viewModel: EssentialsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                TagBadge(
                    text = "🧭 TRAVEL ESSENTIALS",
                    backgroundColor = Cedar.colors.inset,
                    textColor = Cedar.colors.brand
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Practical Information & Guide",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Text(
                    text = "Currency exchange rates, daily budget tiers, visa entry rules, Sunday sacred laws, phrasebook with phonetics, and emergency contacts.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Cedar.colors.textSecondary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 1. Currency Converter Card
        item {
            CurrencyConverterCard(
                amountInput = state.amountInputTop,
                onAmountChange = { viewModel.setAmountInput(it) },
                selectedCode = state.selectedCurrencyCode,
                onSelectCode = { viewModel.setSelectedCurrency(it) },
                convertedValue = viewModel.calculateConverted(state.amountInputTop.toDoubleOrNull() ?: 0.0),
                symbol = state.rates[state.selectedCurrencyCode]?.symbol ?: "$"
            )
        }

        // Budget Tiers Row
        item {
            Text(
                text = "ESTIMATED DAILY BUDGET TIERS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.textMuted,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.budgetTiers.forEach { tier ->
                    val converted = viewModel.calculateConverted(tier.costTOP)
                    val symbol = state.rates[state.selectedCurrencyCode]?.symbol ?: "$"
                    BudgetTierCard(
                        tier = tier,
                        convertedText = "$symbol${String.format(java.util.Locale.US, "%.1f", converted)} ${state.selectedCurrencyCode}"
                    )
                }
            }
        }

        // 2. Emergency Quick-Dial Contacts
        item {
            EmergencyContactsCard(
                contacts = state.emergencyContacts,
                onDial = { phone -> dialNumber(context, phone) }
            )
        }

        // 3. Sunday Sacred Laws Card
        item {
            SundayLawsCard()
        }

        // 4. Visa Entry Rules Card
        item {
            VisaInfoCard(visaList = state.visaInfo)
        }

        // 5. Faka-Tonga Phrasebook Section
        item {
            Text(
                text = "FAKA-TONGA PHRASEBOOK",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.brand,
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = "Common Phrases & Phonetics",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Cedar.colors.textEmphasis
                )
            )
        }

        items(state.phrases, key = { it.tongan }) { phrase ->
            PhraseCard(
                phrase = phrase,
                onShare = { sharePhrase(context, phrase) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CurrencyConverterCard(
    amountInput: String,
    onAmountChange: (String) -> Unit,
    selectedCode: String,
    onSelectCode: (String) -> Unit,
    convertedValue: Double,
    symbol: String
) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "💱 TONGAN PA'ANGA (TOP) CALCULATOR",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.brand,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Currency Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("USD", "AUD", "NZD", "EUR", "GBP").forEach { code ->
                    val isSel = selectedCode == code
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) Cedar.colors.brand else Cedar.colors.inset)
                            .border(BorderStroke(1.dp, if (isSel) Cedar.colors.brand else Cedar.colors.borderSubtle), RoundedCornerShape(6.dp))
                            .clickable { onSelectCode(code) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = code,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else Cedar.colors.textPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Amount Input Field
            OutlinedTextField(
                value = amountInput,
                onValueChange = onAmountChange,
                label = { Text("Amount in Pa'anga (\$T / TOP)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = CedarShapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Cedar.colors.brand,
                    unfocusedBorderColor = Cedar.colors.borderDistinct,
                    focusedLabelColor = Cedar.colors.brand
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Output Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Cedar.colors.inset)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "EQUIVALENT IN $selectedCode",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Cedar.colors.textMuted
                        )
                    )
                    Text(
                        text = "$symbol${String.format(java.util.Locale.US, "%.2f", convertedValue)} $selectedCode",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.brand
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetTierCard(
    tier: online.codyuhi.tongantourism.domain.BudgetTier,
    convertedText: String
) {
    OutlinedCard(
        shape = CedarShapes.small,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tier.label,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Text(
                    text = tier.desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.textSecondary
                    )
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${tier.costTOP.toInt()} TOP",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.brand
                    )
                )
                Text(
                    text = "≈ $convertedText",
                    style = CedarMonoStyle.copy(
                        color = Cedar.colors.textMuted
                    )
                )
            }
        }
    }
}

@Composable
private fun EmergencyContactsCard(
    contacts: List<Triple<String, String, String>>,
    onDial: (String) -> Unit
) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, CedarPalette.Rust.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🚨 EMERGENCY CONTACTS (1-TAP QUICK DIAL)",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CedarPalette.Rust,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            contacts.forEach { (name, phone, desc) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Cedar.colors.inset)
                        .clickable { onDial(phone) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Cedar.colors.textEmphasis
                            )
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Cedar.colors.textSecondary
                            )
                        )
                    }
                    CedarButton(
                        text = phone,
                        onClick = { onDial(phone) },
                        icon = "📞",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SundayLawsCard() {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⛪ SUNDAY SACRED SABBATH LAWS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.brand,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Under the Constitution of Tonga, Sunday is kept holy and sacred across the Kingdom. All commercial business, motorized water sports, organized tours, and heavy labor are strictly prohibited.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Cedar.colors.textSecondary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tip for Visitors: Sunday is a tranquil day of community fellowship. Visitors are warmly welcomed to attend local church services (such as the Centenary Church in Nuku'alofa) to hear world-class multi-part Polynesian choral harmonies and share a family feast.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Cedar.colors.brand,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun VisaInfoCard(visaList: List<online.codyuhi.tongantourism.domain.VisaInfo>) {
    OutlinedCard(
        shape = CedarShapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = Cedar.colors.surface),
        border = BorderStroke(1.dp, Cedar.colors.borderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🛂 VISA & ENTRY REQUIREMENTS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Cedar.colors.textMuted,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            visaList.forEach { info ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = info.region,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Cedar.colors.textEmphasis
                        )
                    )
                    Text(
                        text = "• Rule: ${info.rule}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Cedar.colors.brand,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "• Req: ${info.requirements}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Cedar.colors.textSecondary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PhraseCard(
    phrase: Phrase,
    onShare: () -> Unit
) {
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = phrase.tongan,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Cedar.colors.brand
                    )
                )
                Text(
                    text = phrase.english,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Cedar.colors.textEmphasis
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Pronounce: [ ${phrase.pronunciation} ]",
                    style = CedarMonoStyle.copy(
                        color = CedarPalette.Rust
                    )
                )
                Text(
                    text = "Usage: ${phrase.usage}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Cedar.colors.textMuted
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Cedar.colors.inset)
                    .clickable { onShare() }
                    .padding(8.dp)
            ) {
                Text(text = "📤", fontSize = 16.sp)
            }
        }
    }
}

private fun dialNumber(context: Context, phone: String) {
    val clean = phone.replace(" ", "").replace("-", "")
    val intent = Intent(Intent.ACTION_DIAL, "tel:$clean".toUri())
    context.startActivity(intent)
}

private fun sharePhrase(context: Context, phrase: Phrase) {
    val text = "Tongan Phrase: '${phrase.tongan}' means '${phrase.english}' (Pronounced: ${phrase.pronunciation}). Usage: ${phrase.usage}."
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Phrase via"))
}
