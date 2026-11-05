package online.codyuhi.tongantourism.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.codyuhi.tongantourism.domain.Phrase
import java.util.Locale
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

data class PhraseAudioPlaybackState(
    val playingTongan: String? = null,
    val activeSyllableIndex: Int = -1,
    val isPlaying: Boolean = false
)

/**
 * 100% Offline Audio Engine for Faka-Tonga Phrasebook Pronunciation
 * Combines Android TextToSpeech with Procedural Acoustic Formant Synthesis Fallback.
 */
class TonganPhraseAudioEngine(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _playbackState = MutableStateFlow(PhraseAudioPlaybackState())
    val playbackState: StateFlow<PhraseAudioPlaybackState> = _playbackState.asStateFlow()

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var activeJob: Job? = null
    private var activeAudioTrack: AudioTrack? = null

    init {
        initTts()
    }

    private fun initTts() {
        try {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady = true
                    tts?.setSpeechRate(0.85f)
                    tts?.setPitch(1.0f)
                    val toLocale = Locale.Builder().setLanguage("to").setRegion("TO").build()
                    if (tts?.isLanguageAvailable(toLocale) == TextToSpeech.LANG_AVAILABLE) {
                        tts?.language = toLocale
                    } else {
                        tts?.language = Locale.US
                    }
                }
            }
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    /**
     * Parse pronunciation string into individual syllables for visual highlighting
     */
    fun extractSyllables(pronunciation: String): List<String> {
        return pronunciation.split(Regex("[ -]+")).filter { it.isNotBlank() }
    }

    fun playPhrase(phrase: Phrase) {
        if (_playbackState.value.playingTongan == phrase.tongan && _playbackState.value.isPlaying) {
            stopAudio()
            return
        }

        stopAudio()

        val syllables = extractSyllables(phrase.pronunciation)

        activeJob = scope.launch {
            _playbackState.value = PhraseAudioPlaybackState(
                playingTongan = phrase.tongan,
                activeSyllableIndex = 0,
                isPlaying = true
            )

            if (isTtsReady && tts != null) {
                playWithTts(phrase, syllables)
            } else {
                playWithAcousticSynthesizer(phrase, syllables)
            }
        }
    }

    private suspend fun playWithTts(phrase: Phrase, syllables: List<String>) {
        val utteranceId = "tonga_phrase_${System.currentTimeMillis()}"
        val textToSpeak = phrase.tongan.replace("'", "")

        // Animate syllables across the duration
        val sylDurationMs = 280L
        val sylJob = scope.launch {
            syllables.forEachIndexed { idx, _ ->
                _playbackState.value = _playbackState.value.copy(activeSyllableIndex = idx)
                delay(sylDurationMs)
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                scope.launch {
                    stopAudio()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                scope.launch {
                    playWithAcousticSynthesizer(phrase, syllables)
                }
            }
        })

        val result = tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        if (result != TextToSpeech.SUCCESS) {
            sylJob.cancel()
            playWithAcousticSynthesizer(phrase, syllables)
            return
        }

        // Safety timeout in case TTS listener does not fire onDone
        val maxWaitMs = (syllables.size * sylDurationMs) + 600L
        delay(maxWaitMs)
        stopAudio()
    }

    private suspend fun playWithAcousticSynthesizer(phrase: Phrase, syllables: List<String>) {
        val sampleRate = 44100

        syllables.forEachIndexed { idx, sylText ->
            if (!_playbackState.value.isPlaying) return@forEachIndexed
            _playbackState.value = _playbackState.value.copy(activeSyllableIndex = idx)

            val isStressed = sylText == sylText.uppercase() && sylText.length > 1
            val hasGlottal = sylText.contains("'") || (idx > 0 && phrase.tongan.contains("'"))

            val durationSec = if (isStressed) 0.32 else 0.22
            val numSamples = (sampleRate * durationSec).toInt()

            // Frequency envelope: warm baritone pitch
            val baseFreq = if (isStressed) 180.0 else 155.0
            val pcm = ShortArray(numSamples)
            var phase = 0.0

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val freq = baseFreq - (t * 15.0)
                phase += 2.0 * PI * freq / sampleRate

                // Formant simulation: fundamental + 2nd + 3rd harmonic
                val sample = sin(phase) * 0.6 + sin(phase * 2.0) * 0.3 + sin(phase * 3.0) * 0.15

                // Envelope
                val gain = when {
                    t < 0.03 -> (t / 0.03) * 0.4
                    t > durationSec - 0.04 -> ((durationSec - t) / 0.04) * 0.4
                    else -> 0.4
                }

                pcm[i] = (sample * gain * 32767.0).coerceIn(-32768.0, 32767.0).toInt().toShort()
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            activeAudioTrack = track

            try {
                if (hasGlottal) {
                    delay(50L) // Glottal stop silence
                }
                track.write(pcm, 0, pcm.size)
                track.play()
                delay((durationSec * 1000).toLong() + 30L)
            } catch (_: Exception) {
            } finally {
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        }

        stopAudio()
    }

    fun stopAudio() {
        activeJob?.cancel()
        activeJob = null

        try {
            tts?.stop()
        } catch (_: Exception) {}

        try {
            activeAudioTrack?.stop()
            activeAudioTrack?.release()
        } catch (_: Exception) {}
        activeAudioTrack = null

        _playbackState.value = PhraseAudioPlaybackState()
    }

    fun release() {
        stopAudio()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isTtsReady = false
    }
}
