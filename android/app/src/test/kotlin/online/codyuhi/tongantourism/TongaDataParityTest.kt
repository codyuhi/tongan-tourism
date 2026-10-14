package online.codyuhi.tongantourism

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.InputStreamReader

class TongaDataParityTest {

    private val repo = TongaDataRepository()

    @Test
    fun `islands parity matches web dataset`() {
        assertEquals(4, repo.islands.size)
        val tongatapu = repo.islands.find { it.id == "tongatapu" }
        assertNotNull(tongatapu)
        assertEquals("Tongatapu", tongatapu?.name)
        assertEquals(4, tongatapu?.highlights?.size)

        val vavau = repo.islands.find { it.id == "vavau" }
        assertNotNull(vavau)
        assertEquals("Vava'u", vavau?.name)
        assertEquals("Neiafu", vavau?.capital)

        val haapai = repo.islands.find { it.id == "haapai" }
        assertNotNull(haapai)
        assertEquals("Ha'apai", haapai?.name)

        val eua = repo.islands.find { it.id == "eua" }
        assertNotNull(eua)
        assertEquals("'Eua", eua?.name)
    }

    @Test
    fun `culture items parity matches web dataset`() {
        assertEquals(4, repo.culture.size)
        val kava = repo.culture.find { it.id == "faikava" }
        assertNotNull(kava)
        assertEquals("Faikava (The Kava Ceremony)", kava?.title)

        val cuisine = repo.culture.find { it.id == "cuisine" }
        assertNotNull(cuisine)
        assertEquals(4, cuisine?.dishes?.size)
        assertEquals("'Ota Ika", cuisine?.dishes?.first()?.name)
    }

    @Test
    fun `whales season calendar matches web dataset`() {
        assertEquals(6, repo.whales.season.size)
        assertEquals("June", repo.whales.season.first().month)
        assertEquals("November", repo.whales.season.last().month)
        assertEquals(4, repo.whales.rules.size)
        assertEquals(3, repo.whales.facts.size)
    }

    @Test
    fun `phrasebook contains 8 phrases with pronunciations`() {
        assertEquals(8, repo.phrases.size)
        assertEquals("Mālō e lelei", repo.phrases[0].tongan)
        assertEquals("mah-LOH eh leh-LEH-ee", repo.phrases[0].pronunciation)
    }

    @Test
    fun `golden fixture json matches stats`() {
        val stream = javaClass.classLoader?.getResourceAsStream("golden_tonga_data.json")
        assertNotNull("Golden fixture should be present", stream)
        stream?.use { s ->
            val jsonText = InputStreamReader(s).readText()
            val parsed = Json.parseToJsonElement(jsonText).jsonObject
            val statsObj = parsed["stats"]?.jsonObject
            assertNotNull(statsObj)
            assertEquals("171", statsObj?.get("islandsCount")?.jsonPrimitive?.content)
            assertEquals("45", statsObj?.get("inhabitedCount")?.jsonPrimitive?.content)
            assertEquals("1,000+", statsObj?.get("whalesAnnually")?.jsonPrimitive?.content)
            assertEquals("3,000+", statsObj?.get("historyYears")?.jsonPrimitive?.content)
            assertEquals("Nuku'alofa", statsObj?.get("capital")?.jsonPrimitive?.content)
        }
    }
}
