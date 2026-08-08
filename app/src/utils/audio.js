// Web Audio API Synthesizer & Sound Effects for Tonga Tourism

class SoundEngine {
  constructor() {
    this.ctx = null;
    this.oceanGain = null;
    this.isOceanPlaying = false;
    this.oceanNoiseNode = null;
  }

  initContext() {
    if (!this.ctx) {
      const AudioCtx = window.AudioContext || window.webkitAudioContext;
      this.ctx = new AudioCtx();
    }
    if (this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
  }

  // Toggle ambient ocean wave noise generator
  toggleOceanWaves(enable) {
    this.initContext();

    if (enable && !this.isOceanPlaying) {
      this.startOcean();
    } else if (!enable && this.isOceanPlaying) {
      this.stopOcean();
    }
    return this.isOceanPlaying;
  }

  startOcean() {
    const bufferSize = this.ctx.sampleRate * 2;
    const noiseBuffer = this.ctx.createBuffer(1, bufferSize, this.ctx.sampleRate);
    const output = noiseBuffer.getChannelData(0);

    // Pink noise generation for realistic ocean wave acoustics
    let b0 = 0, b1 = 0, b2 = 0, b3 = 0, b4 = 0, b5 = 0, b6 = 0;
    for (let i = 0; i < bufferSize; i++) {
      const white = Math.random() * 2 - 1;
      b0 = 0.99886 * b0 + white * 0.0555179;
      b1 = 0.99332 * b1 + white * 0.0750759;
      b2 = 0.96900 * b2 + white * 0.1538520;
      b3 = 0.86650 * b3 + white * 0.3104856;
      b4 = 0.55000 * b4 + white * 0.5329522;
      b5 = -0.7616 * b5 - white * 0.0168980;
      output[i] = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362;
      output[i] *= 0.03; // Low volume background
      b6 = white * 0.115926;
    }

    const whiteNoise = this.ctx.createBufferSource();
    whiteNoise.buffer = noiseBuffer;
    whiteNoise.loop = true;

    // Lowpass filter for deep ocean rumble
    const filter = this.ctx.createBiquadFilter();
    filter.type = 'lowpass';
    filter.frequency.setValueAtTime(300, this.ctx.currentTime);

    // Wave swell LFO
    const lfo = this.ctx.createOscillator();
    lfo.type = 'sine';
    lfo.frequency.setValueAtTime(0.12, this.ctx.currentTime); // Wave every ~8 seconds

    const lfoGain = this.ctx.createGain();
    lfoGain.gain.setValueAtTime(150, this.ctx.currentTime);

    lfo.connect(filter.frequency);

    this.oceanGain = this.ctx.createGain();
    this.oceanGain.gain.setValueAtTime(0.08, this.ctx.currentTime);

    whiteNoise.connect(filter);
    filter.connect(this.oceanGain);
    this.oceanGain.connect(this.ctx.destination);

    whiteNoise.start();
    lfo.start();

    this.oceanNoiseNode = whiteNoise;
    this.isOceanPlaying = true;
  }

  stopOcean() {
    if (this.oceanGain && this.ctx) {
      this.oceanGain.gain.exponentialRampToValueAtTime(0.0001, this.ctx.currentTime + 1);
      setTimeout(() => {
        if (this.oceanNoiseNode) {
          try { this.oceanNoiseNode.stop(); } catch(e){}
        }
        this.isOceanPlaying = false;
      }, 1000);
    } else {
      this.isOceanPlaying = false;
    }
  }

  // Play synthetic Humpback Whale song call
  playWhaleSong() {
    this.initContext();

    const osc = this.ctx.createOscillator();
    const gain = this.ctx.createGain();

    osc.type = 'sine';

    const now = this.ctx.currentTime;
    // Sweeping frequency call characteristic of humpback whale songs (250Hz -> 550Hz -> 180Hz)
    osc.frequency.setValueAtTime(280, now);
    osc.frequency.exponentialRampToValueAtTime(580, now + 1.2);
    osc.frequency.exponentialRampToValueAtTime(210, now + 2.5);

    gain.gain.setValueAtTime(0.001, now);
    gain.gain.exponentialRampToValueAtTime(0.2, now + 0.5);
    gain.gain.exponentialRampToValueAtTime(0.001, now + 2.8);

    // Reverb delay effect
    const delay = this.ctx.createDelay();
    delay.delayTime.value = 0.4;
    const feedback = this.ctx.createGain();
    feedback.gain.value = 0.4;

    osc.connect(gain);
    gain.connect(this.ctx.destination);
    gain.connect(delay);
    delay.connect(feedback);
    feedback.connect(delay);
    delay.connect(this.ctx.destination);

    osc.start(now);
    osc.stop(now + 3.0);
  }

  // Speak Faka-Tonga phrase using Speech Synthesis
  speakPhrase(phraseText) {
    if (!('speechSynthesis' in window)) return;
    window.speechSynthesis.cancel();

    const utterance = new SpeechSynthesisUtterance(phraseText);
    utterance.rate = 0.85; // Slightly slower for clarity
    utterance.pitch = 1.0;
    utterance.lang = 'sm-WS'; // Samoan or Maori fallback accent if Tongan is unavailable

    window.speechSynthesis.speak(utterance);
  }
}

export const soundEngine = new SoundEngine();
