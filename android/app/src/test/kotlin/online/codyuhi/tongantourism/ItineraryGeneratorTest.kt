package online.codyuhi.tongantourism

import kotlinx.coroutines.flow.flowOf
import online.codyuhi.tongantourism.data.db.ItineraryDao
import online.codyuhi.tongantourism.data.db.ItineraryEntity
import online.codyuhi.tongantourism.data.repository.ItineraryRepository
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ItineraryGeneratorTest {

    private lateinit var itineraryRepository: ItineraryRepository

    // Mock DAO for generator unit tests
    private val mockDao = object : ItineraryDao {
        override fun getAllItineraries() = flowOf(emptyList<ItineraryEntity>())
        override suspend fun getItineraryById(id: String) = null
        override suspend fun insertItinerary(itinerary: ItineraryEntity) {}
        override suspend fun deleteItineraryById(id: String) {}
        override fun countItineraries() = flowOf(0)
    }

    @Before
    fun setup() {
        val dataRepo = TongaDataRepository()
        itineraryRepository = ItineraryRepository(mockDao, dataRepo)
    }

    @Test
    fun `generate 7-day itinerary creates exactly 7 days`() {
        val itin = itineraryRepository.generateItinerary(
            daysCount = 7,
            selectedIslands = listOf("tongatapu", "vavau"),
            vibe = "Balanced Explorer"
        )

        assertEquals(7, itin.days.size)
        assertEquals(1, itin.days.first().day)
        assertEquals(7, itin.days.last().day)
        assertTrue(itin.days.first().title.contains("Arrival", ignoreCase = true))
        assertTrue(itin.days.last().title.contains("Departure", ignoreCase = true))
    }

    @Test
    fun `generate 3-day itinerary handles short duration`() {
        val itin = itineraryRepository.generateItinerary(
            daysCount = 3,
            selectedIslands = listOf("haapai"),
            vibe = "Relaxed Beach & Solitude"
        )

        assertEquals(3, itin.days.size)
        assertEquals("Ha'apai", itin.days[1].island)
    }

    @Test
    fun `generate 14-day itinerary covers all selected islands`() {
        val itin = itineraryRepository.generateItinerary(
            daysCount = 14,
            selectedIslands = listOf("tongatapu", "vavau", "haapai", "eua"),
            vibe = "Whale & Ocean Adventure"
        )

        assertEquals(14, itin.days.size)
        assertTrue(itin.title.contains("Whale", ignoreCase = true))
    }
}
