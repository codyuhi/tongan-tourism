import { TONGA_DATA } from './data/tongaData.js';
import { soundEngine } from './utils/audio.js';

// Application State
const state = {
  currentView: 'discover',
  // index.html applies the saved or OS theme before first paint; start from that
  theme: document.documentElement.getAttribute('data-theme') === 'light' ? 'light' : 'dark',
  savedItineraries: JSON.parse(localStorage.getItem('tonga_saved_trips') || '[]'),
  currentFilter: 'all',
  liveRates: TONGA_DATA.currency.defaultRates
};

// View Transition Helper
function navigateToView(viewId) {
  if (state.currentView === viewId) return;

  const updateDOM = () => {
    document.querySelectorAll('.view-section').forEach(sec => {
      sec.classList.remove('active');
    });

    const targetSection = document.getElementById(`${viewId}-view`);
    if (targetSection) {
      targetSection.classList.add('active');
    }

    document.querySelectorAll('.nav-item button').forEach(btn => {
      btn.classList.toggle('active', btn.dataset.view === viewId);
    });

    document.querySelectorAll('.mobile-drawer-btn').forEach(btn => {
      btn.classList.toggle('active', btn.dataset.view === viewId);
    });

    document.querySelectorAll('.bottom-nav-btn').forEach(btn => {
      btn.classList.toggle('active', btn.dataset.view === viewId);
    });

    const drawer = document.getElementById('mobile-drawer');
    const backdrop = document.getElementById('mobile-drawer-backdrop');
    if (drawer && drawer.classList.contains('open')) {
      drawer.classList.remove('open');
      backdrop?.classList.remove('open');
    }

    state.currentView = viewId;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  if (!document.startViewTransition) {
    updateDOM();
    return;
  }

  document.startViewTransition(updateDOM);
}

// Live Tonga Local Time Clock (UTC+13)
function initTongaClock() {
  const clockEl = document.getElementById('tonga-clock');
  if (!clockEl) return;

  function updateClock() {
    const now = new Date();
    const utcMs = now.getTime() + (now.getTimezoneOffset() * 60000);
    const tongaTime = new Date(utcMs + (13 * 3600000));
    
    const hours = String(tongaTime.getHours()).padStart(2, '0');
    const mins = String(tongaTime.getMinutes()).padStart(2, '0');
    clockEl.textContent = `${hours}:${mins}`;
  }

  updateClock();
  setInterval(updateClock, 1000);
}

// Live Dynamic Weather Fetcher for Nuku'alofa, Tonga (-21.1789, -175.1982)
async function fetchLiveTongaWeather() {
  const weatherNumEl = document.getElementById('tonga-weather');
  const weatherLabelEl = document.getElementById('tonga-weather-label');
  if (!weatherNumEl) return;

  try {
    const response = await fetch('https://api.open-meteo.com/v1/forecast?latitude=-21.1789&longitude=-175.1982&current_weather=true');
    if (response.ok) {
      const data = await response.json();
      if (data && data.current_weather) {
        const tempC = Math.round(data.current_weather.temperature);
        const tempF = Math.round((tempC * 9/5) + 32);
        const windSpeed = data.current_weather.windspeed;
        
        weatherNumEl.textContent = `${tempC}°C / ${tempF}°F`;
        if (weatherLabelEl) {
          weatherLabelEl.textContent = `Nuku'alofa Live (${windSpeed} km/h wind)`;
        }
      }
    }
  } catch (err) {
    console.warn('Weather API offline, using fallback estimates', err);
    weatherNumEl.textContent = '27°C / 81°F';
  }
}

// Live Dynamic Currency Rates Fetcher (TOP Base Currency)
async function fetchLiveCurrencyRates() {
  const badgeEl = document.getElementById('live-rate-badge');
  try {
    const response = await fetch('https://open.er-api.com/v6/latest/TOP');
    if (response.ok) {
      const data = await response.json();
      if (data && data.rates) {
        state.liveRates = {
          USD: parseFloat(data.rates.USD?.toFixed(4) || 0.42),
          AUD: parseFloat(data.rates.AUD?.toFixed(4) || 0.64),
          NZD: parseFloat(data.rates.NZD?.toFixed(4) || 0.70),
          EUR: parseFloat(data.rates.EUR?.toFixed(4) || 0.39),
          GBP: parseFloat(data.rates.GBP?.toFixed(4) || 0.33)
        };
        if (badgeEl) badgeEl.textContent = '🟢 Live Exchange Rates';
        updateCurrencyDisplay();
      }
    }
  } catch (err) {
    console.warn('Currency API unavailable, using standard rates', err);
    if (badgeEl) badgeEl.textContent = 'Standard Bank Rates';
  }
}

// Render Islands View
function renderIslands(filter = 'all') {
  const container = document.getElementById('islands-card-container');
  if (!container) return;

  const filteredIslands = filter === 'all' 
    ? TONGA_DATA.islands 
    : TONGA_DATA.islands.filter(i => i.id === filter);

  container.innerHTML = filteredIslands.map(island => `
    <div class="glass-card">
      <div class="island-card-img-wrap">
        <img src="${island.image}" alt="${island.name}" class="island-card-img">
        <div class="island-card-badge">${island.group}</div>
      </div>
      <div class="island-card-body">
        <div class="island-card-tagline">${island.tagline}</div>
        <h3 class="island-card-title">${island.name}</h3>
        <p class="island-card-desc">${island.description}</p>
        
        <div class="highlights-pill-list">
          ${island.bestFor.map(b => `<span class="highlight-pill">✨ ${b}</span>`).join('')}
        </div>

        <button class="btn-secondary btn-detail-island" data-id="${island.id}" style="width:100%; justify-content:center;">
          View Island Details ➔
        </button>
      </div>
    </div>
  `).join('');

  container.querySelectorAll('.btn-detail-island').forEach(btn => {
    btn.addEventListener('click', () => openIslandDetailModal(btn.dataset.id));
  });
}

// Island Detail Modal Controller
function openIslandDetailModal(islandId) {
  const island = TONGA_DATA.islands.find(i => i.id === islandId);
  if (!island) return;

  const modal = document.getElementById('detail-modal');
  const title = document.getElementById('modal-title');
  const body = document.getElementById('modal-body');

  title.textContent = `${island.name} — ${island.tagline}`;
  body.innerHTML = `
    <div style="margin-bottom:1.5rem;">
      <img src="${island.secondaryImage || island.image}" alt="${island.name}" style="width:100%; height:240px; object-fit:cover; border-radius:12px; margin-bottom:1rem;">
      <p style="color:var(--text-muted); font-size:1rem; line-height:1.6;">${island.description}</p>
    </div>

    <div style="background:var(--glass-bg); padding:1rem; border-radius:8px; margin-bottom:1.5rem; display:flex; gap:1rem; flex-wrap:wrap; border:1px solid var(--glass-border);">
      <div><strong>Capital:</strong> ${island.capital}</div>
      <div><strong>Airport:</strong> ${island.airport}</div>
      <div><strong>Inter-Island Logistics:</strong> ${island.flightTimeFromCapital}</div>
    </div>

    <h4 class="font-royal" style="color:var(--lagoon-turquoise); margin-bottom:1rem;">Top Attractions & Hidden Gems</h4>
    <div style="display:flex; flex-direction:column; gap:1rem;">
      ${island.highlights.map(h => `
        <div style="background:var(--bg-surface-solid); border:1px solid var(--glass-border); padding:1rem; border-radius:8px;">
          <h5 style="color:var(--gold-warm); font-size:1.05rem; font-weight:700;">📍 ${h.title}</h5>
          <p style="color:var(--text-muted); font-size:0.9rem; margin-top:0.35rem;">${h.desc}</p>
        </div>
      `).join('')}
    </div>
  `;

  modal.showModal();
}

// Render Culture View
function renderCulture() {
  const container = document.getElementById('culture-card-container');
  if (!container) return;

  container.innerHTML = TONGA_DATA.culture.map(item => `
    <div class="glass-card">
      <img src="${item.image}" alt="${item.title}" class="culture-card-img">
      <div class="island-card-body">
        <div class="island-card-tagline">${item.tagline}</div>
        <h3 class="island-card-title">${item.title}</h3>
        <p class="island-card-desc">${item.summary}</p>
        
        ${item.etiquetteTips ? `
          <div style="background:color-mix(in srgb, var(--lagoon-turquoise) 8%, transparent); border-left:3px solid var(--lagoon-turquoise); padding:0.75rem 1rem; border-radius:6px; margin:1rem 0;">
            <strong style="color:var(--lagoon-turquoise); font-size:0.85rem; display:block; margin-bottom:0.25rem;">PROPER ETIQUETTE:</strong>
            <ul style="padding-left:1rem; color:var(--text-muted); font-size:0.85rem;">
              ${item.etiquetteTips.map(tip => `<li>${tip}</li>`).join('')}
            </ul>
          </div>
        ` : ''}

        ${item.dishes ? `
          <div style="display:flex; flex-direction:column; gap:0.5rem; margin-top:1rem;">
            ${item.dishes.map(d => `
              <div style="background:var(--glass-bg); border:1px solid var(--glass-border); padding:0.6rem 0.8rem; border-radius:6px;">
                <span style="color:var(--gold-warm); font-weight:700;">🍽️ ${d.name}</span> — <span style="color:var(--text-muted); font-size:0.85rem;">${d.desc}</span>
              </div>
            `).join('')}
          </div>
        ` : ''}
      </div>
    </div>
  `).join('');
}

// Render Whale Sanctuary View
function renderWhales() {
  const seasonContainer = document.getElementById('whale-season-container');
  const rulesList = document.getElementById('whale-rules-list');
  const factsList = document.getElementById('whale-facts-list');

  if (seasonContainer) {
    seasonContainer.innerHTML = TONGA_DATA.whales.season.map(s => {
      const isPeak = s.status.includes('Peak') || s.status.includes('Active');
      return `
        <div class="month-card ${isPeak ? 'peak' : ''}">
          <h4 class="font-royal" style="color:${isPeak ? 'var(--lagoon-turquoise)' : 'var(--text-main)'};">${s.month}</h4>
          <span style="font-size:0.75rem; font-weight:700; color:var(--gold-warm); text-transform:uppercase;">${s.status}</span>
          <p style="font-size:0.8rem; color:var(--text-muted); margin-top:0.5rem; line-height:1.4;">${s.description}</p>
        </div>
      `;
    }).join('');
  }

  if (rulesList) {
    rulesList.innerHTML = TONGA_DATA.whales.rules.map(r => `<li>${r}</li>`).join('');
  }

  if (factsList) {
    factsList.innerHTML = TONGA_DATA.whales.facts.map(f => `<li>${f}</li>`).join('');
  }
}

// Itinerary Plan Generator (Client-side JS logic)
function generateItineraryPlan() {
  const duration = parseInt(document.getElementById('plan-duration').value, 10);
  const vibe = document.getElementById('plan-vibe').value;
  const selectedIslands = Array.from(document.querySelectorAll('#plan-island-checkboxes input:checked')).map(cb => cb.value);

  const container = document.getElementById('itinerary-days-container');
  const titleDisplay = document.getElementById('plan-title-display');
  
  titleDisplay.textContent = `${duration}-Day ${vibe} Plan`;

  const days = [];
  for (let d = 1; d <= duration; d++) {
    const assignedIsland = selectedIslands[(d - 1) % selectedIslands.length] || 'Tongatapu';
    
    let title = `Exploring ${assignedIsland}`;
    let desc = `Discover the highlights, local cuisine, and coastal vistas of ${assignedIsland}.`;

    if (d === 1) {
      title = `Arrival in Tongatapu & Capital Warmup`;
      desc = `Land at Fua'amotu Airport, check into your hotel, explore Nuku'alofa waterfront and Talamahu market.`;
    } else if (d === duration) {
      title = `Farewell Tonga & Departure`;
      desc = `Shop for hand-carved souvenirs, enjoy a final coconut smoothie, and head to the airport.`;
    } else if (assignedIsland === "Vava'u") {
      title = `Vava'u Whale Swim & Cave Kayaking`;
      desc = `Guided boat excursion into Port of Refuge to swim with humpback whales and explore Swallow's Cave.`;
    } else if (assignedIsland === "Ha'apai") {
      title = `Ha'apai Sandbar Picnic & Snorkel`;
      desc = `Relax on endless white sand beaches and snorkel untouched coral reef gardens.`;
    } else if (assignedIsland === "'Eua") {
      title = `'Eua Rainforest Trek & Cliff Lookout`;
      desc = `Hike through 'Eua National Park, see giant strangler fig trees, and look down at high sea cliffs.`;
    }

    days.push({ day: d, island: assignedIsland, title, desc });
  }

  container.innerHTML = days.map(day => `
    <div class="day-plan-card">
      <div style="display:flex; justify-content:space-between; align-items:center;">
        <span style="font-weight:700; color:var(--lagoon-turquoise); font-size:0.9rem;">DAY ${day.day} • ${day.island.toUpperCase()}</span>
        <span style="font-size:0.75rem; color:var(--text-muted); background:var(--glass-bg); padding:0.2rem 0.5rem; border-radius:4px;">Scheduled</span>
      </div>
      <h4 style="font-size:1.1rem; font-weight:700; margin:0.4rem 0 0.2rem;">${day.title}</h4>
      <p style="color:var(--text-muted); font-size:0.9rem; line-height:1.5;">${day.desc}</p>
    </div>
  `).join('');

  state.currentPlan = { id: `plan-${Date.now()}`, duration, vibe, islands: selectedIslands, days };
}

// Generate Offline-Friendly Markdown Format
function formatPlanAsMarkdown(plan) {
  const dateStr = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
  const islandsList = Array.isArray(plan.islands) ? plan.islands.join(', ') : (plan.islands || 'Tongatapu');

  let md = `# 🌴 Kingdom of Tonga Travel Itinerary & Offline Checklist\n\n`;
  md += `**Trip**: ${plan.duration}-Day ${plan.vibe}\n`;
  md += `**Archipelagos**: ${islandsList}\n`;
  md += `**Generated**: ${dateStr}\n\n`;
  md += `---\n\n`;

  md += `## 📅 Day-by-Day Schedule\n\n`;
  plan.days.forEach(d => {
    md += `### [ ] Day ${d.day} • ${d.island.toUpperCase()}\n`;
    md += `**${d.title}**\n\n`;
    md += `${d.desc}\n\n`;
  });

  md += `---\n\n`;
  md += `## ⛵ Inter-Island Logistics & Boat / Flight Travel\n\n`;
  md += `- **Domestic Air (${TONGA_DATA.interIslandLogistics.domesticAir.operator})**: ${TONGA_DATA.interIslandLogistics.domesticAir.routes}\n`;
  md += `  - Baggage: ${TONGA_DATA.interIslandLogistics.domesticAir.baggage}\n`;
  md += `  - Sunday Notice: ${TONGA_DATA.interIslandLogistics.domesticAir.sundayRule}\n`;
  md += `- **Ferries (${TONGA_DATA.interIslandLogistics.ferries.operator})**: ${TONGA_DATA.interIslandLogistics.ferries.routes}\n`;
  md += `  - Advice: ${TONGA_DATA.interIslandLogistics.ferries.advice}\n`;
  md += `- **Water Taxis**: ${TONGA_DATA.interIslandLogistics.localBoats.description}\n\n`;

  md += `---\n\n`;
  md += `## ⛪ Sunday Observance (Sāpate Law) & Etiquette\n\n`;
  md += `- **Constitutional Sacred Rest Day**: ${TONGA_DATA.sundayObservance.constitutionalLaw}\n`;
  md += `- **Choral Church Services**: ${TONGA_DATA.sundayObservance.services}\n`;
  md += `- **Modest Dress Code**: ${TONGA_DATA.sundayObservance.dressCode}\n`;
  md += `- **Tourist Resorts**: ${TONGA_DATA.sundayObservance.resorts}\n\n`;

  md += `---\n\n`;
  md += `## 🚨 Emergency Contacts & Essential Field Logistics (+676)\n\n`;
  md += `- **Police Emergency**: ${TONGA_DATA.emergencyLogistics.police.phone} (${TONGA_DATA.emergencyLogistics.police.notes})\n`;
  md += `- **Ambulance & Hospital**: ${TONGA_DATA.emergencyLogistics.ambulance.phone} (${TONGA_DATA.emergencyLogistics.ambulance.notes})\n`;
  md += `- **Fire & Rescue**: ${TONGA_DATA.emergencyLogistics.fire.phone}\n`;
  md += `- **Maritime Search & Rescue**: ${TONGA_DATA.emergencyLogistics.marineRescue.phone} (${TONGA_DATA.emergencyLogistics.marineRescue.notes})\n`;
  md += `- **Outer Island Hospitals**:\n`;
  TONGA_DATA.emergencyLogistics.outerHospitals.forEach(h => {
    md += `  - ${h.name}: ${h.phone}\n`;
  });
  md += `- **Currency / Cash**: ${TONGA_DATA.emergencyLogistics.currencyNotes}\n`;
  md += `- **Electricity**: ${TONGA_DATA.emergencyLogistics.electricity}\n\n`;

  md += `---\n\n`;
  md += `## 🗣️ Key Faka-Tonga Travel Phrases\n\n`;
  TONGA_DATA.phrases.forEach(p => {
    md += `- **"${p.tongan}"** (${p.english}): Pronounced _"${p.pronunciation}"_ • ${p.usage}\n`;
  });

  md += `\n---\n*Exported from Kingdom of Tonga Tourism (offline travel field guide)*\n`;
  return md;
}

// Render Ink-Friendly Print Sheet for @media print
function renderPrintSheet(plan) {
  const sheet = document.getElementById('printable-itinerary-sheet');
  if (!sheet) return;

  const dateStr = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
  const islandsList = Array.isArray(plan.islands) ? plan.islands.join(', ') : (plan.islands || 'Tongatapu');

  sheet.innerHTML = `
    <div class="print-header">
      <div>
        <h1 class="print-title">Kingdom of Tonga • Travel Itinerary & Field Guide</h1>
        <div class="print-meta">
          <strong>Trip:</strong> ${plan.duration}-Day ${plan.vibe} &nbsp;|&nbsp;
          <strong>Archipelagos:</strong> ${islandsList} &nbsp;|&nbsp;
          <strong>Export Date:</strong> ${dateStr}
        </div>
      </div>
      <div style="text-align:right; font-size:8.5pt; color:#444444;">
        Official Travel Sanctuary Guide<br>
        <strong>tonga.lan.codyuhi.online</strong>
      </div>
    </div>

    <!-- Daily Schedule -->
    <div class="print-section">
      <div class="print-section-title">📅 Daily Itinerary Schedule</div>
      <div class="print-days-grid">
        ${plan.days.map(d => `
          <div class="print-day-card">
            <div class="print-day-header">
              <span>☐ DAY ${d.day} • ${d.island.toUpperCase()}</span>
              <span style="font-size:8.5pt; font-weight:600; color:#0b3d2e;">${d.title}</span>
            </div>
            <p class="print-day-desc">${d.desc}</p>
          </div>
        `).join('')}
      </div>
    </div>

    <!-- Logistics & Sunday Etiquette Grid -->
    <div class="print-section">
      <div class="print-section-title">⛵ Transport Logistics & Cultural Observance</div>
      <div class="print-info-grid">
        <div class="print-info-box">
          <h4>✈️ Inter-Island Air & Ferry Logistics</h4>
          <p style="margin-bottom:0.35rem;"><strong>Lulutai Airlines:</strong> ${TONGA_DATA.interIslandLogistics.domesticAir.routes}</p>
          <p style="margin-bottom:0.35rem;"><strong>Luggage Limit:</strong> ${TONGA_DATA.interIslandLogistics.domesticAir.baggage}</p>
          <p style="margin-bottom:0.35rem;"><strong>Sunday Notice:</strong> ${TONGA_DATA.interIslandLogistics.domesticAir.sundayRule}</p>
          <p><strong>Ferries:</strong> ${TONGA_DATA.interIslandLogistics.ferries.routes}</p>
        </div>
        <div class="print-info-box">
          <h4>⛪ Sunday Observance (Sāpate Law)</h4>
          <p style="margin-bottom:0.35rem;"><strong>Constitutional Rest:</strong> ${TONGA_DATA.sundayObservance.constitutionalLaw}</p>
          <p style="margin-bottom:0.35rem;"><strong>Church Services:</strong> ${TONGA_DATA.sundayObservance.services}</p>
          <p style="margin-bottom:0.35rem;"><strong>Dress Code:</strong> ${TONGA_DATA.sundayObservance.dressCode}</p>
          <p><strong>Resorts:</strong> ${TONGA_DATA.sundayObservance.resorts}</p>
        </div>
      </div>
    </div>

    <!-- Emergency & Phrases Grid -->
    <div class="print-section">
      <div class="print-section-title">🚨 Emergency Directory & Essential Phrases</div>
      <div class="print-info-grid">
        <div class="print-info-box">
          <h4>📞 Emergency & Medical Contacts (+676)</h4>
          <p style="margin-bottom:0.25rem;"><strong>Police:</strong> ${TONGA_DATA.emergencyLogistics.police.phone}</p>
          <p style="margin-bottom:0.25rem;"><strong>Ambulance / Vaiola Hospital:</strong> ${TONGA_DATA.emergencyLogistics.ambulance.phone}</p>
          <p style="margin-bottom:0.25rem;"><strong>Maritime Rescue:</strong> ${TONGA_DATA.emergencyLogistics.marineRescue.phone}</p>
          <p style="margin-bottom:0.25rem;"><strong>Outer Hospitals:</strong> Vava'u (70-201) | Ha'apai (60-224) | 'Eua (50-101)</p>
          <p style="margin-bottom:0.25rem;"><strong>Currency:</strong> ${TONGA_DATA.emergencyLogistics.currencyNotes}</p>
          <p><strong>Electricity:</strong> ${TONGA_DATA.emergencyLogistics.electricity}</p>
        </div>
        <div class="print-info-box">
          <h4>🗣️ Useful Faka-Tonga Phrases</h4>
          <table class="print-phrases-table">
            <thead>
              <tr>
                <th>Tongan</th>
                <th>English</th>
                <th>Pronunciation</th>
              </tr>
            </thead>
            <tbody>
              ${TONGA_DATA.phrases.slice(0, 6).map(p => `
                <tr>
                  <td><strong>${p.tongan}</strong></td>
                  <td>${p.english}</td>
                  <td><em>${p.pronunciation}</em></td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="print-footer">
      <span>Kingdom of Tonga Tourism Guide • Sāpate Observance • Whales Sanctuary</span>
      <span>Keep safe and enjoy your Polynesian journey • 'Ofa atu!</span>
    </div>
  `;
}

// Print / Export Itinerary
function printPlan(plan) {
  const targetPlan = plan || state.currentPlan;
  if (!targetPlan) {
    generateItineraryPlan();
  }
  renderPrintSheet(targetPlan || state.currentPlan);
  window.print();
}

// Copy Markdown to Clipboard
function copyPlanMarkdown(plan) {
  const targetPlan = plan || state.currentPlan;
  if (!targetPlan) {
    generateItineraryPlan();
  }
  const md = formatPlanAsMarkdown(targetPlan || state.currentPlan);

  if (navigator.clipboard && navigator.clipboard.writeText) {
    navigator.clipboard.writeText(md)
      .then(() => showToast('📋 Offline itinerary copied to clipboard in Markdown!'))
      .catch(() => fallbackCopy(md));
  } else {
    fallbackCopy(md);
  }
}

function fallbackCopy(text) {
  const ta = document.createElement('textarea');
  ta.value = text;
  ta.style.position = 'fixed';
  ta.style.opacity = '0';
  document.body.appendChild(ta);
  ta.select();
  try {
    document.execCommand('copy');
    showToast('📋 Offline itinerary copied to clipboard in Markdown!');
  } catch {
    showToast('Failed to copy to clipboard.');
  }
  document.body.removeChild(ta);
}

// Download Markdown File
function downloadPlanMarkdown(plan) {
  const targetPlan = plan || state.currentPlan;
  if (!targetPlan) {
    generateItineraryPlan();
  }
  const activePlan = targetPlan || state.currentPlan;
  const md = formatPlanAsMarkdown(activePlan);
  const blob = new Blob([md], { type: 'text/markdown;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  const safeName = `${activePlan.duration}-Day-${activePlan.vibe}`.toLowerCase().replace(/[^a-z0-9]+/g, '-');
  a.href = url;
  a.download = `tonga-itinerary-${safeName}.md`;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
  showToast('📥 Downloaded offline itinerary (.md)!');
}

// Toast Feedback Notification
let toastTimer = null;
function showToast(msg) {
  const toast = document.getElementById('toast-notification');
  if (!toast) return;
  toast.textContent = msg;
  toast.classList.add('show');
  if (toastTimer) clearTimeout(toastTimer);
  toastTimer = setTimeout(() => {
    toast.classList.remove('show');
  }, 3500);
}

// Save Current Plan to LocalStorage
function saveCurrentPlan() {
  if (!state.currentPlan) {
    generateItineraryPlan();
  }

  const existingIndex = state.savedItineraries.findIndex(p => p.id === state.currentPlan.id);
  if (existingIndex < 0) {
    state.savedItineraries.push(state.currentPlan);
  }

  localStorage.setItem('tonga_saved_trips', JSON.stringify(state.savedItineraries));
  updateSavedTripBadge();
  showToast('✨ Itinerary saved! Access anytime in Saved Trips.');
}

// Update Badge Count
function updateSavedTripBadge() {
  const count = state.savedItineraries.length;
  const badgeCount = document.getElementById('saved-trip-count');
  if (badgeCount) {
    badgeCount.textContent = count;
  }
  const badgeCountMobile = document.getElementById('saved-trip-count-mobile');
  if (badgeCountMobile) {
    badgeCountMobile.textContent = count;
  }
}

// Open Saved Trips Drawer Modal
function openSavedTripsModal() {
  const modal = document.getElementById('itinerary-modal');
  const body = document.getElementById('saved-itinerary-body');

  if (state.savedItineraries.length === 0) {
    body.innerHTML = `
      <div style="text-align:center; padding:3rem 1rem; color:var(--text-muted);">
        <p style="font-size:1.1rem; margin-bottom:1rem;">You haven't saved any itineraries yet!</p>
        <button class="btn-primary" id="btn-modal-go-planner">Go to Itinerary Planner ➔</button>
      </div>
    `;
    body.querySelector('#btn-modal-go-planner')?.addEventListener('click', () => {
      modal.close();
      navigateToView('planner');
    });
  } else {
    body.innerHTML = state.savedItineraries.map((plan, idx) => `
      <div style="background:var(--bg-surface-solid); border:1px solid var(--glass-border); padding:1.25rem; border-radius:12px; margin-bottom:1rem;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.75rem; flex-wrap:wrap; gap:0.5rem;">
          <h4 class="font-royal" style="color:var(--lagoon-turquoise); margin:0;">${plan.duration}-Day ${plan.vibe}</h4>
          <div style="display:flex; gap:0.4rem; flex-wrap:wrap;">
            <button class="btn-secondary btn-load-plan" data-idx="${idx}" style="padding:0.25rem 0.6rem; font-size:0.75rem;" title="Load in Planner">Load</button>
            <button class="btn-secondary btn-print-plan-item" data-idx="${idx}" style="padding:0.25rem 0.6rem; font-size:0.75rem;" title="Print or PDF">🖨️ Print</button>
            <button class="btn-secondary btn-copy-plan-item" data-idx="${idx}" style="padding:0.25rem 0.6rem; font-size:0.75rem;" title="Copy Markdown">📋 Copy</button>
            <button class="btn-secondary btn-download-plan-item" data-idx="${idx}" style="padding:0.25rem 0.6rem; font-size:0.75rem;" title="Download .md">📥 .md</button>
            <button class="btn-delete-plan" data-idx="${idx}" style="background:none; border:none; color:var(--primary-royal); cursor:pointer; font-weight:700; font-size:0.85rem; padding:0.25rem 0.5rem;">Delete</button>
          </div>
        </div>
        <p style="color:var(--text-muted); font-size:0.85rem; margin-bottom:0.5rem;">Islands: ${plan.islands.join(', ')}</p>
        <div style="display:flex; flex-direction:column; gap:0.4rem;">
          ${plan.days.map(d => `
            <div style="font-size:0.85rem; color:var(--text-main);">
              <strong>Day ${d.day} (${d.island}):</strong> ${d.title}
            </div>
          `).join('')}
        </div>
      </div>
    `).join('');

    body.querySelectorAll('.btn-load-plan').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = parseInt(e.target.dataset.idx, 10);
        const plan = state.savedItineraries[idx];
        if (plan) {
          state.currentPlan = plan;
          const container = document.getElementById('itinerary-days-container');
          const titleDisplay = document.getElementById('plan-title-display');
          if (titleDisplay) titleDisplay.textContent = `${plan.duration}-Day ${plan.vibe} Plan`;
          if (container) {
            container.innerHTML = plan.days.map(day => `
              <div class="day-plan-card">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                  <span style="font-weight:700; color:var(--lagoon-turquoise); font-size:0.9rem;">DAY ${day.day} • ${day.island.toUpperCase()}</span>
                  <span style="font-size:0.75rem; color:var(--text-muted); background:var(--glass-bg); padding:0.2rem 0.5rem; border-radius:4px;">Scheduled</span>
                </div>
                <h4 style="font-size:1.1rem; font-weight:700; margin:0.4rem 0 0.2rem;">${day.title}</h4>
                <p style="color:var(--text-muted); font-size:0.9rem; line-height:1.5;">${day.desc}</p>
              </div>
            `).join('');
          }
          modal.close();
          navigateToView('planner');
          showToast('Loaded itinerary into Planner!');
        }
      });
    });

    body.querySelectorAll('.btn-print-plan-item').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = parseInt(e.target.dataset.idx, 10);
        const plan = state.savedItineraries[idx];
        if (plan) {
          modal.close();
          printPlan(plan);
        }
      });
    });

    body.querySelectorAll('.btn-copy-plan-item').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = parseInt(e.target.dataset.idx, 10);
        const plan = state.savedItineraries[idx];
        if (plan) copyPlanMarkdown(plan);
      });
    });

    body.querySelectorAll('.btn-download-plan-item').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = parseInt(e.target.dataset.idx, 10);
        const plan = state.savedItineraries[idx];
        if (plan) downloadPlanMarkdown(plan);
      });
    });

    body.querySelectorAll('.btn-delete-plan').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const idx = parseInt(e.target.dataset.idx, 10);
        state.savedItineraries.splice(idx, 1);
        localStorage.setItem('tonga_saved_trips', JSON.stringify(state.savedItineraries));
        updateSavedTripBadge();
        openSavedTripsModal();
      });
    });
  }

  modal.showModal();
}

// Update Currency Display
function updateCurrencyDisplay() {
  const currencyGrid = document.getElementById('currency-output-grid');
  const topInput = document.getElementById('curr-input-top');
  if (!currencyGrid || !topInput) return;

  const val = parseFloat(topInput.value) || 0;
  currencyGrid.innerHTML = Object.entries(state.liveRates).map(([curr, rate]) => `
    <div style="background:var(--bg-surface-solid); padding:0.75rem 1rem; border-radius:8px; border:1px solid var(--glass-border); box-shadow:var(--shadow-sm);">
      <span style="font-size:0.75rem; color:var(--text-muted); display:block; font-weight:600;">${curr} Equivalent</span>
      <span style="font-size:1.25rem; font-weight:700; color:var(--lagoon-turquoise);">${(val * rate).toFixed(2)} ${curr}</span>
    </div>
  `).join('');
}

// Render Travel Essentials (Currency, Written Phrasebook, Visa)
function renderEssentials() {
  const budgetContainer = document.getElementById('budget-tiers-container');
  const phrasebookContainer = document.getElementById('phrasebook-list');
  const visaContainer = document.getElementById('visa-info-list');
  const topInput = document.getElementById('curr-input-top');

  if (topInput) {
    topInput.addEventListener('input', updateCurrencyDisplay);
    updateCurrencyDisplay();
  }

  if (budgetContainer) {
    budgetContainer.innerHTML = Object.values(TONGA_DATA.currency.budgets).map(b => `
      <div style="background:var(--bg-surface-solid); border:1px solid var(--glass-border); padding:0.9rem 1rem; border-radius:8px;">
        <div style="display:flex; justify-content:space-between; font-weight:700; margin-bottom:0.2rem;">
          <span>${b.label}</span>
          <span style="color:var(--gold-warm);">$T ${b.costTOP} TOP / day</span>
        </div>
        <p style="color:var(--text-muted); font-size:0.85rem; margin:0;">${b.desc}</p>
      </div>
    `).join('');
  }

  // Written Pronunciation Phrasebook
  if (phrasebookContainer) {
    phrasebookContainer.innerHTML = TONGA_DATA.phrases.map(p => `
      <div style="background:var(--bg-surface-solid); border:1px solid var(--glass-border); padding:0.9rem 1.1rem; border-radius:8px; box-shadow:var(--shadow-sm);">
        <div style="display:flex; justify-content:space-between; align-items:baseline; margin-bottom:0.25rem;">
          <span style="font-weight:800; color:var(--lagoon-turquoise); font-size:1.1rem;">"${p.tongan}"</span>
          <span style="font-size:0.85rem; font-weight:600; color:var(--gold-warm);">${p.english}</span>
        </div>
        <div style="color:var(--text-muted); font-size:0.85rem;">
          🗣️ Pronounced: <em>"${p.pronunciation}"</em> • <span style="color:var(--text-dim);">${p.usage}</span>
        </div>
      </div>
    `).join('');
  }

  if (visaContainer) {
    visaContainer.innerHTML = TONGA_DATA.visaInfo.map(v => `
      <div style="background:var(--bg-surface-solid); border:1px solid var(--glass-border); padding:1rem; border-radius:8px;">
        <h5 style="color:var(--gold-warm); font-weight:700; margin-bottom:0.25rem;">${v.region}</h5>
        <div style="color:var(--text-main); font-weight:600; font-size:0.9rem; margin-bottom:0.25rem;">${v.rule}</div>
        <p style="color:var(--text-muted); font-size:0.85rem; margin:0;">Requirements: ${v.requirements}</p>
      </div>
    `).join('');
  }
}

// Setup Event Listeners & Initialization
const THEME_COLORS = { light: '#ebe7df', dark: '#161614' };

function applyTheme() {
  document.documentElement.setAttribute('data-theme', state.theme);
  document.querySelector('meta[name="theme-color"]')?.setAttribute('content', THEME_COLORS[state.theme]);
  const themeBtn = document.getElementById('btn-theme-toggle');
  if (themeBtn) themeBtn.textContent = state.theme === 'dark' ? '☀️' : '🌙';
}

function initApp() {
  applyTheme();

  initTongaClock();
  fetchLiveTongaWeather();
  fetchLiveCurrencyRates();

  // Initial renders
  renderIslands('all');
  renderCulture();
  renderWhales();
  renderEssentials();
  generateItineraryPlan();
  updateSavedTripBadge();

  // Navigation Button Handlers
  document.querySelectorAll('[data-view]').forEach(el => {
    el.addEventListener('click', () => {
      const view = el.dataset.view;
      if (view) navigateToView(view);
    });
  });

  // Action Buttons
  document.getElementById('hero-btn-explore')?.addEventListener('click', () => navigateToView('islands'));
  document.getElementById('hero-btn-whales')?.addEventListener('click', () => navigateToView('whales'));

  document.querySelectorAll('[data-action="go-whales"]').forEach(b => b.addEventListener('click', () => navigateToView('whales')));
  document.querySelectorAll('[data-action="go-islands"]').forEach(b => b.addEventListener('click', () => navigateToView('islands')));
  document.querySelectorAll('[data-action="go-culture"]').forEach(b => b.addEventListener('click', () => navigateToView('culture')));

  // Island Filter Buttons
  document.querySelectorAll('#island-filter-bar .filter-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('#island-filter-bar .filter-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      renderIslands(btn.dataset.filter);
    });
  });

  // Interactive SVG Map Pin Clicks
  document.querySelectorAll('.map-island-pin').forEach(pin => {
    pin.addEventListener('click', () => {
      const islandId = pin.dataset.island;
      navigateToView('islands');
      
      document.querySelectorAll('#island-filter-bar .filter-btn').forEach(b => {
        b.classList.toggle('active', b.dataset.filter === islandId);
      });
      renderIslands(islandId);
    });
  });

  // Whale Synth Soundscape Trigger
  document.getElementById('btn-play-whale-synth')?.addEventListener('click', () => {
    soundEngine.playWhaleSong();
  });

  // Itinerary Generator Form Controls
  document.getElementById('btn-generate-plan')?.addEventListener('click', generateItineraryPlan);
  document.getElementById('btn-save-current-plan')?.addEventListener('click', saveCurrentPlan);
  document.getElementById('btn-print-plan')?.addEventListener('click', () => printPlan());
  document.getElementById('btn-copy-plan-md')?.addEventListener('click', () => copyPlanMarkdown());
  document.getElementById('btn-download-plan-md')?.addEventListener('click', () => downloadPlanMarkdown());
  document.getElementById('btn-saved-trips')?.addEventListener('click', openSavedTripsModal);

  // Mobile Navigation Drawer Toggle Handlers
  const mobileDrawer = document.getElementById('mobile-drawer');
  const mobileBackdrop = document.getElementById('mobile-drawer-backdrop');
  const openMobileDrawer = () => {
    mobileDrawer?.classList.add('open');
    mobileBackdrop?.classList.add('open');
  };
  const closeMobileDrawer = () => {
    mobileDrawer?.classList.remove('open');
    mobileBackdrop?.classList.remove('open');
  };

  document.getElementById('btn-mobile-menu')?.addEventListener('click', openMobileDrawer);
  document.getElementById('btn-mobile-more')?.addEventListener('click', openMobileDrawer);
  document.getElementById('btn-mobile-drawer-close')?.addEventListener('click', closeMobileDrawer);
  mobileBackdrop?.addEventListener('click', closeMobileDrawer);
  document.getElementById('btn-saved-trips-mobile')?.addEventListener('click', () => {
    closeMobileDrawer();
    openSavedTripsModal();
  });

  // Modal Closers
  document.getElementById('btn-close-modal')?.addEventListener('click', () => {
    document.getElementById('detail-modal').close();
  });
  document.getElementById('btn-close-saved-modal')?.addEventListener('click', () => {
    document.getElementById('itinerary-modal').close();
  });

  // Theme Switcher Toggle
  const themeBtn = document.getElementById('btn-theme-toggle');
  if (themeBtn) {
    themeBtn.setAttribute('aria-label', 'Toggle color theme');
    themeBtn.addEventListener('click', () => {
      state.theme = state.theme === 'dark' ? 'light' : 'dark';
      try {
        localStorage.setItem('tonga_theme', state.theme);
      } catch {
        // storage unavailable: the toggle still applies for this visit
      }
      applyTheme();
    });
  }

  // Follow the OS until the user picks a theme with the toggle
  window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
    let saved = null;
    try {
      saved = localStorage.getItem('tonga_theme');
    } catch {}
    if (saved !== 'light' && saved !== 'dark') {
      state.theme = e.matches ? 'dark' : 'light';
      applyTheme();
    }
  });
}

document.addEventListener('DOMContentLoaded', initApp);
