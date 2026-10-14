package online.codyuhi.tongantourism.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.codyuhi.tongantourism.data.db.ItineraryDao
import online.codyuhi.tongantourism.data.db.ItineraryEntity
import online.codyuhi.tongantourism.domain.ItineraryDay
import online.codyuhi.tongantourism.domain.SavedItinerary
import java.util.UUID

class ItineraryRepository(
    private val dao: ItineraryDao,
    private val dataRepo: TongaDataRepository
) {
    val savedItineraries: Flow<List<SavedItinerary>> = dao.getAllItineraries().map { entities ->
        entities.map { it.toDomain() }
    }

    val savedCount: Flow<Int> = dao.countItineraries()

    suspend fun saveItinerary(itinerary: SavedItinerary) {
        dao.insertItinerary(ItineraryEntity.fromDomain(itinerary))
    }

    suspend fun deleteItinerary(id: String) {
        dao.deleteItineraryById(id)
    }

    fun generateItinerary(
        daysCount: Int,
        selectedIslands: List<String>,
        vibe: String
    ): SavedItinerary {
        val islandsToUse = if (selectedIslands.isEmpty()) listOf("tongatapu", "vavau") else selectedIslands
        val islandModels = islandsToUse.mapNotNull { dataRepo.getIslandById(it) }

        val days = mutableListOf<ItineraryDay>()

        // Day 1: Arrival in Tongatapu
        days.add(
            ItineraryDay(
                day = 1,
                island = "Tongatapu",
                title = "Arrival & Royal Waterfront",
                desc = "Touch down at Fua'amotu International (TBU). Settle into your Nuku'alofa waterfront stay, stroll along Vuna Road to view the Royal Palace, and explore fresh tropical fruits at Talamahu Market."
            )
        )

        var currentDay = 2
        val remainingDays = daysCount - 2 // Save day N for departure

        // Distribute remaining days across selected islands
        val targetIslands = if (islandModels.isEmpty()) dataRepo.islands else islandModels
        val highlightsPool = targetIslands.flatMap { isl ->
            isl.highlights.map { hl -> isl.name to hl }
        }.toMutableList()

        while (currentDay < daysCount && highlightsPool.isNotEmpty()) {
            val (islandName, hl) = highlightsPool.removeAt(0)
            val dayDesc = when (vibe) {
                "Relaxed Beach & Solitude" -> "Enjoy a gentle morning at ${hl.title} (${hl.desc}). Spend the afternoon soaking in calm lagoon breezes and crystal-clear waters."
                "Culture & Heritage" -> "Immerse in local tradition visiting ${hl.title} (${hl.desc}). Engage with friendly locals, discover ancient craftsmanship, and share a bowl of kava."
                "Whale & Ocean Adventure" -> "Embark on an ocean expedition around ${hl.title}. Snorkel vibrant barrier reefs and witness humpback whales breaching across the deep bay."
                else -> "Explore ${hl.title} — ${hl.desc}. Savor authentic Tongan cuisine for dinner with local grilled ocean fish and fresh coconut cream."
            }
            days.add(
                ItineraryDay(
                    day = currentDay,
                    island = islandName,
                    title = hl.title,
                    desc = dayDesc
                )
            )
            currentDay++
        }

        // If days still remain, fill with island exploration
        while (currentDay < daysCount) {
            val islandName = targetIslands[(currentDay % targetIslands.size)].name
            days.add(
                ItineraryDay(
                    day = currentDay,
                    island = islandName,
                    title = "$islandName Island Discovery",
                    desc = "Wander through secluded turquoise bays, sample fresh 'Ota Ika from seaside markets, and experience the warm hospitality of the Friendly Islands."
                )
            )
            currentDay++
        }

        // Final Day: Farewell
        days.add(
            ItineraryDay(
                day = daysCount,
                island = "Tongatapu",
                title = "Handcrafts, Farewell & Departure",
                desc = "Pick up hand-carved bone carvings, woven Ta'ovala fine mats, and fresh vanilla pods in Nuku'alofa before transferring to Fua'amotu Airport for your onward journey."
            )
        )

        val title = when (vibe) {
            "Whale & Ocean Adventure" -> "$daysCount-Day Whale Sanctuary & Outer Atolls Expedition"
            "Culture & Heritage" -> "$daysCount-Day Ancient Polynesian Heritage & Royal Kingdom"
            "Relaxed Beach & Solitude" -> "$daysCount-Day Tropical Sanctuary & Island Solitude"
            else -> "$daysCount-Day Kingdom of Tonga Odyssey"
        }

        return SavedItinerary(
            id = UUID.randomUUID().toString(),
            title = title,
            duration = "$daysCount Days",
            vibe = vibe,
            islands = targetIslands.map { it.name },
            days = days
        )
    }
}
