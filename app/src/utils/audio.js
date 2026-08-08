// Web Audio API Synthesizer for Whale Soundscapes

class SoundEngine {
  constructor() {
    this.ctx = null;
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

  // Play synthetic Humpback Whale song call acoustics
  playWhaleSong() {
    this.initContext();

    const osc = this.ctx.createOscillator();
    const gain = this.ctx.createGain();

    osc.type = 'sine';

    const now = this.ctx.currentTime;
    osc.frequency.setValueAtTime(280, now);
    osc.frequency.exponentialRampToValueAtTime(580, now + 1.2);
    osc.frequency.exponentialRampToValueAtTime(210, now + 2.5);

    gain.gain.setValueAtTime(0.001, now);
    gain.gain.exponentialRampToValueAtTime(0.2, now + 0.5);
    gain.gain.exponentialRampToValueAtTime(0.001, now + 2.8);

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
}

export const soundEngine = new SoundEngine();
