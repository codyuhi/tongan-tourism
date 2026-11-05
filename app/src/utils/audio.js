// Web Audio API Synthesizer for Whale Soundscapes & Faka-Tonga Phonetic Audio Engine

class SoundEngine {
  constructor() {
    this.ctx = null;
    this.activeUtterance = null;
    this.activeNodes = [];
    this.activeTimeouts = [];
    this.currentPlayingIdx = null;
    this.playbackMode = 'auto'; // 'auto' | 'speech' | 'acoustic'
  }

  initContext() {
    if (!this.ctx) {
      const AudioCtx = window.AudioContext || window.webkitAudioContext;
      if (AudioCtx) {
        this.ctx = new AudioCtx();
      }
    }
    if (this.ctx && this.ctx.state === 'suspended') {
      this.ctx.resume().catch(() => {});
    }
    return this.ctx;
  }

  // Play synthetic Humpback Whale song call acoustics
  playWhaleSong() {
    const ctx = this.initContext();
    if (!ctx) return;

    const osc = ctx.createOscillator();
    const gain = ctx.createGain();

    osc.type = 'sine';

    const now = ctx.currentTime;
    osc.frequency.setValueAtTime(280, now);
    osc.frequency.exponentialRampToValueAtTime(580, now + 1.2);
    osc.frequency.exponentialRampToValueAtTime(210, now + 2.5);

    gain.gain.setValueAtTime(0.001, now);
    gain.gain.exponentialRampToValueAtTime(0.2, now + 0.5);
    gain.gain.exponentialRampToValueAtTime(0.001, now + 2.8);

    const delay = ctx.createDelay();
    delay.delayTime.value = 0.4;
    const feedback = ctx.createGain();
    feedback.gain.value = 0.4;

    osc.connect(gain);
    gain.connect(ctx.destination);
    gain.connect(delay);
    delay.connect(feedback);
    feedback.connect(delay);
    delay.connect(ctx.destination);

    osc.start(now);
    osc.stop(now + 3.0);
  }

  // Stop any active phrase pronunciation playback
  stopPhraseAudio() {
    // Clear timeouts
    this.activeTimeouts.forEach(t => clearTimeout(t));
    this.activeTimeouts = [];

    // Cancel speech synthesis
    if (window.speechSynthesis) {
      try {
        window.speechSynthesis.cancel();
      } catch (_) {}
    }
    this.activeUtterance = null;

    // Stop and disconnect Web Audio nodes
    this.activeNodes.forEach(node => {
      try {
        if (typeof node.stop === 'function') node.stop();
        if (typeof node.disconnect === 'function') node.disconnect();
      } catch (_) {}
    });
    this.activeNodes = [];

    const prevIdx = this.currentPlayingIdx;
    this.currentPlayingIdx = null;
    return prevIdx;
  }

  /**
   * Play Faka-Tonga Phrase Pronunciation
   * @param {Object} phrase - The phrase data object
   * @param {number} phraseIdx - Index of phrase in list
   * @param {Function} onSyllable - Callback (syllableIdx)
   * @param {Function} onEnd - Callback ()
   */
  playPhraseAudio(phrase, phraseIdx, onSyllable = () => {}, onEnd = () => {}) {
    this.initContext();
    this.stopPhraseAudio();
    this.currentPlayingIdx = phraseIdx;

    const mode = this.playbackMode;
    const canUseSpeech = ('speechSynthesis' in window) && (mode === 'auto' || mode === 'speech');

    if (canUseSpeech) {
      this._playWithSpeechSynthesis(phrase, phraseIdx, onSyllable, onEnd, () => {
        // Fallback to procedural synth on error
        this._playProceduralSynth(phrase, phraseIdx, onSyllable, onEnd);
      });
    } else {
      this._playProceduralSynth(phrase, phraseIdx, onSyllable, onEnd);
    }
  }

  // Web Speech API with tuned Polynesian phonetics
  _playWithSpeechSynthesis(phrase, phraseIdx, onSyllable, onEnd, fallback) {
    if (!window.speechSynthesis) {
      fallback();
      return;
    }

    try {
      window.speechSynthesis.cancel();

      // Use audioPhonetic respelling for natural open vowels, or fallback to tongan
      const textToSpeak = phrase.audioPhonetic || phrase.tongan.replace(/['’]/g, '');
      const utterance = new SpeechSynthesisUtterance(textToSpeak);
      this.activeUtterance = utterance;

      // Select voice: prioritize Polynesian/Pacific, AU/NZ, or clear natural voices
      const voices = window.speechSynthesis.getVoices();
      const preferredVoice = voices.find(v => /tonga|maori|samoan|hawaiian|to-|sm-|mi-|haw/i.test(v.lang || v.name))
        || voices.find(v => /en-NZ|en-AU/i.test(v.lang))
        || voices.find(v => /natural|siri|google/i.test(v.name) && /en/i.test(v.lang))
        || voices.find(v => /en-US|en-GB/i.test(v.lang));

      if (preferredVoice) {
        utterance.voice = preferredVoice;
      }

      utterance.rate = 0.85; // Relaxed Polynesian cadence
      utterance.pitch = 1.0;
      utterance.volume = 0.95;

      const syllables = phrase.syllables || [phrase.pronunciation];
      const totalSyllables = syllables.length;

      // Estimate total duration based on text length and cadence (~140ms per phoneme char at 0.85 rate)
      const estimatedDurationMs = Math.max(1200, textToSpeak.length * 115);
      const sylDurationMs = estimatedDurationMs / totalSyllables;

      // Schedule real-time syllable highlights
      syllables.forEach((_, idx) => {
        const timer = setTimeout(() => {
          if (this.currentPlayingIdx === phraseIdx) {
            onSyllable(idx);
          }
        }, idx * sylDurationMs);
        this.activeTimeouts.push(timer);
      });

      let ended = false;
      const finish = () => {
        if (ended) return;
        ended = true;
        this.activeTimeouts.forEach(t => clearTimeout(t));
        this.activeTimeouts = [];
        this.activeUtterance = null;
        if (this.currentPlayingIdx === phraseIdx) {
          this.currentPlayingIdx = null;
          onEnd();
        }
      };

      utterance.onend = finish;
      utterance.onerror = () => {
        if (!ended) {
          ended = true;
          this.activeTimeouts.forEach(t => clearTimeout(t));
          this.activeTimeouts = [];
          fallback();
        }
      };

      // Workaround for mobile browser speech synthesis timeout bug
      const safetyTimeout = setTimeout(finish, estimatedDurationMs + 1500);
      this.activeTimeouts.push(safetyTimeout);

      window.speechSynthesis.speak(utterance);
    } catch (_) {
      fallback();
    }
  }

  // 100% Client-Side Procedural Formant Synthesizer for Polynesian Phonetics
  _playProceduralSynth(phrase, phraseIdx, onSyllable, onEnd) {
    const ctx = this.initContext();
    if (!ctx) {
      onEnd();
      return;
    }

    const syllables = phrase.syllables || [phrase.pronunciation];
    const now = ctx.currentTime + 0.05;
    let offset = 0;

    // Master bus
    const masterGain = ctx.createGain();
    masterGain.gain.setValueAtTime(0.35, now);
    masterGain.connect(ctx.destination);
    this.activeNodes.push(masterGain);

    // Formant frequency lookup table for pure Polynesian vowels [a, e, i, o, u]
    const VOWEL_FORMANTS = {
      a: { f1: 750, f2: 1250, f3: 2500 },
      e: { f1: 530, f2: 1850, f3: 2550 },
      i: { f1: 280, f2: 2300, f3: 2950 },
      o: { f1: 500, f2: 950,  f3: 2450 },
      u: { f1: 320, f2: 850,  f3: 2400 }
    };

    syllables.forEach((sylText, idx) => {
      const lower = sylText.toLowerCase();
      const isStressed = sylText === sylText.toUpperCase() && sylText.length > 1;
      const hasGlottal = sylText.includes("'");

      // Glottal stop (fakau'a) adds brief silence before phoneme onset
      if (hasGlottal) {
        offset += 0.06;
      }

      // Syllable duration
      const duration = isStressed ? 0.32 : 0.22;
      const startTime = now + offset;
      const stopTime = startTime + duration;

      // Determine primary vowel
      let vowel = 'a';
      if (/ee|i|kigh|sigh|say/.test(lower)) vowel = 'i';
      else if (/eh|feh|leh|peh/.test(lower)) vowel = 'e';
      else if (/oh|noh|toh/.test(lower)) vowel = 'o';
      else if (/oo|tu|lu/.test(lower)) vowel = 'u';
      else if (/ow|tow/.test(lower)) vowel = 'o';

      const formants = VOWEL_FORMANTS[vowel] || VOWEL_FORMANTS.a;

      // Base pitch (F0): Warm Pacific conversational baritone (150-185 Hz)
      const baseFreq = isStressed ? 180 : 155;
      const endFreq = (idx === syllables.length - 1) ? baseFreq - 20 : baseFreq;

      // Glottal excitation pulse (sawtooth with warmth)
      const osc = ctx.createOscillator();
      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(baseFreq, startTime);
      osc.frequency.exponentialRampToValueAtTime(Math.max(100, endFreq), stopTime);

      // Amplitude envelope
      const env = ctx.createGain();
      env.gain.setValueAtTime(0.001, startTime);
      env.gain.exponentialRampToValueAtTime(isStressed ? 0.45 : 0.32, startTime + 0.025);
      env.gain.exponentialRampToValueAtTime(0.001, stopTime);

      // Resonant formant filters (F1 and F2 form the vowel identity)
      const filter1 = ctx.createBiquadFilter();
      filter1.type = 'bandpass';
      filter1.frequency.setValueAtTime(formants.f1, startTime);
      filter1.Q.setValueAtTime(7.0, startTime);

      const filter2 = ctx.createBiquadFilter();
      filter2.type = 'bandpass';
      filter2.frequency.setValueAtTime(formants.f2, startTime);
      filter2.Q.setValueAtTime(8.0, startTime);

      // If syllable has unvoiced fricatives (f, h), add subtle breath noise
      if (/f|h/.test(lower)) {
        try {
          const bufferSize = Math.floor(ctx.sampleRate * 0.06);
          const noiseBuffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
          const output = noiseBuffer.getChannelData(0);
          for (let i = 0; i < bufferSize; i++) {
            output[i] = Math.random() * 2 - 1;
          }
          const whiteNoise = ctx.createBufferSource();
          whiteNoise.buffer = noiseBuffer;
          const noiseFilter = ctx.createBiquadFilter();
          noiseFilter.type = 'bandpass';
          noiseFilter.frequency.value = 3500;
          noiseFilter.Q.value = 3.0;

          const noiseGain = ctx.createGain();
          noiseGain.gain.setValueAtTime(0.04, startTime);
          noiseGain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.05);

          whiteNoise.connect(noiseFilter);
          noiseFilter.connect(noiseGain);
          noiseGain.connect(masterGain);
          whiteNoise.start(startTime);
          this.activeNodes.push(whiteNoise);
        } catch (_) {}
      }

      osc.connect(env);
      env.connect(filter1);
      env.connect(filter2);
      filter1.connect(masterGain);
      filter2.connect(masterGain);

      osc.start(startTime);
      osc.stop(stopTime + 0.02);

      this.activeNodes.push(osc, env, filter1, filter2);

      // Visual syllable synchronization timer
      const sylDelayMs = (startTime - ctx.currentTime) * 1000;
      const sylTimer = setTimeout(() => {
        if (this.currentPlayingIdx === phraseIdx) {
          onSyllable(idx);
        }
      }, Math.max(0, sylDelayMs));
      this.activeTimeouts.push(sylTimer);

      offset += duration + 0.03;
    });

    // Schedule playback completion
    const totalDurationMs = (now + offset - ctx.currentTime) * 1000;
    const endTimer = setTimeout(() => {
      this.stopPhraseAudio();
      onEnd();
    }, Math.max(0, totalDurationMs));
    this.activeTimeouts.push(endTimer);
  }
}

export const soundEngine = new SoundEngine();
