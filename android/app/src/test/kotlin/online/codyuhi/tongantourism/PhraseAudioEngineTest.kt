package online.codyuhi.tongantourism

import online.codyuhi.tongantourism.audio.PhraseAudioPlaybackState
import online.codyuhi.tongantourism.domain.Phrase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PhraseAudioEngineTest {

    private fun extractSyllables(pronunciation: String): List<String> {
        return pronunciation.split(Regex("[ -]+")).filter { it.isNotBlank() }
    }

    @Test
    fun `extractSyllables splits hyphenated and space-delimited phonetics correctly`() {
        val syl1 = extractSyllables("mah-LOH eh leh-LEH-ee")
        assertEquals(listOf("mah", "LOH", "eh", "leh", "LEH", "ee"), syl1)

        val syl2 = extractSyllables("FEH-feh HAH-keh")
        assertEquals(listOf("FEH", "feh", "HAH", "keh"), syl2)

        val syl3 = extractSyllables("fah-kah-ah-PAH-ah-pah")
        assertEquals(listOf("fah", "kah", "ah", "PAH", "ah", "pah"), syl3)

        val syl4 = extractSyllables("EE-oh")
        assertEquals(listOf("EE", "oh"), syl4)

        val syl5 = extractSyllables("ee-KIGH")
        assertEquals(listOf("ee", "KIGH"), syl5)
    }

    @Test
    fun `phrase audio playback state default is idle`() {
        val state = PhraseAudioPlaybackState()
        assertFalse(state.isPlaying)
        assertEquals(null, state.playingTongan)
        assertEquals(-1, state.activeSyllableIndex)
    }

    @Test
    fun `phrase audio playback state active updates correctly`() {
        val state = PhraseAudioPlaybackState(
            playingTongan = "Mālō e lelei",
            activeSyllableIndex = 2,
            isPlaying = true
        )
        assertTrue(state.isPlaying)
        assertEquals("Mālō e lelei", state.playingTongan)
        assertEquals(2, state.activeSyllableIndex)
    }
}
