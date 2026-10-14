package online.codyuhi.tongantourism.domain

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

data class Highlight(
    val title: String,
    val desc: String
)

data class Island(
    val id: String,
    val name: String,
    val tagline: String,
    val group: String,
    val capital: String,
    val airport: String,
    @DrawableRes val imageRes: Int,
    @DrawableRes val secondaryImageRes: Int,
    val description: String,
    val highlights: List<Highlight>,
    val bestFor: List<String>,
    val flightTimeFromCapital: String,
    val coordX: Int,
    val coordY: Int
)

data class Dish(
    val name: String,
    val desc: String
)

data class CultureItem(
    val id: String,
    val title: String,
    val tagline: String,
    @DrawableRes val imageRes: Int,
    val summary: String,
    val details: List<String> = emptyList(),
    val etiquetteTips: List<String> = emptyList(),
    val dishes: List<Dish> = emptyList()
)

data class WhaleSeason(
    val month: String,
    val status: String,
    val description: String
)

data class WhaleData(
    @DrawableRes val heroImageRes: Int,
    val season: List<WhaleSeason>,
    val rules: List<String>,
    val facts: List<String>
)

data class Phrase(
    val tongan: String,
    val english: String,
    val usage: String,
    val pronunciation: String
)

data class BudgetTier(
    val tierKey: String,
    val label: String,
    val costTOP: Double,
    val desc: String
)

data class VisaInfo(
    val region: String,
    val rule: String,
    val requirements: String
)

data class KingdomStats(
    val islandsCount: Int = 171,
    val inhabitedCount: Int = 45,
    val whalesAnnually: String = "1,000+",
    val historyYears: String = "3,000+",
    val timeZone: String = "UTC+13 (TOT)",
    val capital: String = "Nuku'alofa",
    val currency: String = "Tongan Pa'anga (TOP / \$T)"
)

@Serializable
data class ItineraryDay(
    val day: Int,
    val island: String,
    val title: String,
    val desc: String
)

@Serializable
data class SavedItinerary(
    val id: String,
    val title: String,
    val duration: String,
    val vibe: String,
    val islands: List<String>,
    val days: List<ItineraryDay>,
    val createdAt: Long = System.currentTimeMillis()
)

data class WeatherInfo(
    val tempC: Int,
    val tempF: Int,
    val windSpeedKmH: Double,
    val condition: String,
    val isLive: Boolean
)

data class CurrencyRate(
    val code: String,
    val rate: Double, // 1 TOP = X foreign currency
    val symbol: String
)
