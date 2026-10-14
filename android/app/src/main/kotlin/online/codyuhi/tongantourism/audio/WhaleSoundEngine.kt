package online.codyuhi.tongantourism.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

class WhaleSoundEngine(private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {

    private var activeJob: Job? = null

    fun playWhaleSong() {
        activeJob?.cancel()
        activeJob = scope.launch(Dispatchers.Default) {
            synthesizeAndPlay()
        }
    }

    private fun synthesizeAndPlay() {
        val sampleRate = 44100
        val durationSec = 3.2
        val numSamples = (sampleRate * durationSec).toInt()
        val delaySamples = (sampleRate * 0.4).toInt()
        val feedback = 0.4

        val drySamples = DoubleArray(numSamples)
        var phase = 0.0

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate

            // Frequency envelope: 280 Hz -> 580 Hz (1.2s) -> 210 Hz (2.5s)
            val freq = when {
                t < 1.2 -> 280.0 * (580.0 / 280.0).pow(t / 1.2)
                t < 2.5 -> 580.0 * (210.0 / 580.0).pow((t - 1.2) / 1.3)
                else -> 210.0
            }

            phase += 2.0 * PI * freq / sampleRate

            // Amplitude envelope
            val gain = when {
                t < 0.5 -> 0.25 * (t / 0.5)
                t < 2.8 -> 0.25 * (0.005).pow((t - 0.5) / 2.3)
                else -> 0.0
            }

            drySamples[i] = sin(phase) * gain
        }

        // Apply delay buffer with feedback for cavernous ocean acoustics
        val wetSamples = DoubleArray(numSamples)
        for (i in 0 until numSamples) {
            val echo = if (i >= delaySamples) wetSamples[i - delaySamples] * feedback else 0.0
            wetSamples[i] = (drySamples[i] + echo).coerceIn(-1.0, 1.0)
        }

        val pcm = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            pcm[i] = (wetSamples[i] * 32767.0).toInt().toShort()
        }

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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

        try {
            audioTrack.write(pcm, 0, pcm.size)
            audioTrack.play()
            Thread.sleep((durationSec * 1000).toLong())
        } catch (_: Exception) {
            // Cancelled or interrupted
        } finally {
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
            }
        }
    }
}
