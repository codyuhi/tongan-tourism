package online.codyuhi.tongantourism.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import online.codyuhi.tongantourism.domain.ItineraryDay
import online.codyuhi.tongantourism.domain.SavedItinerary

@Entity(tableName = "saved_itineraries")
data class ItineraryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val duration: String,
    val vibe: String,
    val islandsCommaSeparated: String,
    val daysJson: String,
    val createdAt: Long
) {
    fun toDomain(): SavedItinerary {
        val daysList = try {
            Json.decodeFromString<List<ItineraryDay>>(daysJson)
        } catch (_: Exception) {
            emptyList()
        }
        val islandsList = islandsCommaSeparated.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return SavedItinerary(
            id = id,
            title = title,
            duration = duration,
            vibe = vibe,
            islands = islandsList,
            days = daysList,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(domain: SavedItinerary): ItineraryEntity {
            return ItineraryEntity(
                id = domain.id,
                title = domain.title,
                duration = domain.duration,
                vibe = domain.vibe,
                islandsCommaSeparated = domain.islands.joinToString(","),
                daysJson = Json.encodeToString(domain.days),
                createdAt = domain.createdAt
            )
        }
    }
}
