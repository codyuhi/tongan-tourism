# Native Capability Evaluation: Kingdom of Tonga Tourism

- **Date**: 2026-10-14
- **Web app**: https://tonga-tourism.lan.codyuhi.online (frontend-only SPA, static assets served via Nginx, optional public keyless APIs for live weather and currency rates with instant offline fallbacks)
- **Primary users / devices**: Cody (Galaxy Z Fold 8 Ultra), Lena / family (Galaxy S26+)
- **Status**: Drafted for Cody's approval

## How the app is used today
The Kingdom of Tonga Tourism application is an interactive destination guide, cultural encyclopedia, and trip planner for Tonga's four island groups (*Tongatapu*, *Vava'u*, *Ha'apai*, *'Eua*). Users browse island highlights, review cultural etiquette (Faikava kava ceremonies, Ngatu barkcloth, Lakalaka dances, traditional feasts), check humpback whale swim guidelines and migration calendars, practice Faka-Tonga phrases with pronunciations, plan and save custom multi-day itineraries, and check currency conversions and practical visa/Sunday travel laws. When traveling or exploring offline in the South Pacific, reliable offline access to maps, emergency numbers, cultural rules, and custom itineraries is critical.

## Web limitations that hurt
- **Offline reliability**: In remote South Pacific atolls where roaming cellular and Wi-Fi are spotty or non-existent, the web app depends on browser cache resilience. A dedicated offline native app with local Room persistence guarantees zero failure.
- **Emergency calling**: In an emergency or logistics pinch (e.g. Vaiola Hospital, local police, search & rescue), web links cannot seamlessly open the device's native dialer.
- **Trip sharing**: No native integration with Android's system Sharesheet to send custom itineraries, whale swim rules, or phrases to travel partners via SMS or messaging apps.
- **Screen real estate on Foldables**: Browser chrome, address bars, and web modals do not take full advantage of the Galaxy Z Fold 8 Ultra's ~752dp wide unfolded inner screen.
- **Background audio**: Web Audio API soundscapes halt when switching apps or locking the phone.

## Capability scores
| Capability | Value (0–3) | Cost (S/M/L) | Verdict | Notes |
| :--- | :---: | :---: | :--- | :--- |
| **Offline Room Cache & Persistence** (all 4 island groups, culture, whale guide, phrases, saved itineraries in Room DB) | 3 | M | v1 | Single source of truth. Travel guides must work 100% offline with zero network connectivity. |
| **Foldable Adaptive Layout** (Cover: compact single pane + bottom bar; Inner: ~72dp slim rail + ≥85% wide multi-column content) | 3 | M | v1 | Maximizes Galaxy Z Fold 8 Ultra inner screen with 2-column island grids, split culture viewer, and dual-pane planner. |
| **Emergency Contacts Quick-Dial** (`Intent.ACTION_DIAL`) | 3 | S | v1 | 1-tap phone dialer launch for Vaiola Hospital, Police, Search & Rescue (no dangerous permission). |
| **Interactive Itinerary Planner & Room Storage** | 3 | M | v1 | Generate custom travel schedules, save, edit, and delete in local Room database. |
| **Native Sharesheet Out** (`ACTION_SEND`) | 2 | S | v1 | Share itineraries, whale swim rules, and phrases to travel companions. |
| **Native Polynesian Audio Soundscape** (`AudioTrack` / SoundPool) | 2 | M | v1 | Calming ocean waves, tropical breeze, and acoustic tones. |
| **App Shortcuts** (Itinerary Planner, Whale Sanctuary, Phrasebook) | 2 | S | v1 | Quick access from launcher icon long-press. |
| **Tactile Haptics** (`HapticFeedbackConstants`) | 2 | S | v1 | Subtle haptic pulses on tab changes, itinerary generation, audio toggle. |
| **Currency Converter** (Offline cached rates + live online refresh) | 2 | S | v1 | Real-time calculations across TOP, USD, AUD, NZD, EUR, GBP. |
| **Live Weather & UTC+13 Clock** (Open-Meteo live updates with offline fallback) | 2 | S | v1 | Real-time temperature & wind in Nuku'alofa; works offline seamlessly. |
| **Interactive Offline Vector Map** | 2 | M | later | Full pan/zoom vector tiles with GPS markers. Defer to v2. |
| **Home Screen Widget** (Tonga Clock + Whale Season / Weather) | 1 | M | later | Glance widget showing current Tonga time and whale migration status. |
| **Camera / Photo Attachments to Itineraries** | 1 | M | later | Attach personal trip photos to days in saved itineraries. |
| **Notifications / Trip Reminders** | 0 | — | no | Static travel guide does not require background push alerts. |
| **BiometricPrompt App Lock** | 0 | — | no | Tourism guide contains no sensitive user credentials or health data. |
| **Location / GPS Background Tracking** | 0 | — | no | Guide does not need runtime location permissions in v1. |

## Recommended v1 (beyond web parity)
Web parity first:
- Complete guide across all 4 island groups (*Tongatapu*, *Vava'u*, *Ha'apai*, *'Eua*) with highlights, coordinates, airport logistics, image galleries, and detailed modal cards.
- Culture & Heritage guide: Faikava ceremony rules, Ngatu barkcloth, Lakalaka and Kailao dances, and authentic Tongan feast cuisine ('Ota Ika, Lu Pulu, etc.).
- Whale Sanctuary: Month-by-month humpback migration calendar, ethical swimmer rules, and conservation facts.
- Itinerary Planner: Custom schedule generation by duration, island selection, and vibe, with save/delete functionality.
- Travel Essentials: Offline/live TOP currency converter, visa entry rules, Sunday sacred laws, and Faka-Tonga phrasebook with phonetic pronunciation guides.
- Cedar Design System: Cedar Spruce (`#1F513F`), ecru canvas, Source Serif 4 headings, Inter body, JetBrains Mono tags, and persistent light/dark theme toggle in the top app bar of every screen.

Beyond parity (Native v1 additions):
1. **100% Offline Room Persistence**: Room database for saved itineraries, recent currency conversions, and offline island content.
2. **Foldable Content-First (Galaxy Z Fold 8 Ultra)**: Slim ~72dp navigation rail on wide screens (≥600dp), dedicating ≥85% of screen width to fluid multi-column island cards, side-by-side culture guides, and split-pane itinerary generation.
3. **Emergency Quick-Dial**: Direct 1-tap call links launching Android's system dialer (`Intent.ACTION_DIAL`) for Vaiola Hospital (+676 23-200), Police (922/911), and Maritime Search & Rescue without requiring dangerous phone permissions.
4. **Native Sharesheet Integration**: Share generated itineraries and cultural phrases directly to messaging apps via Android `ACTION_SEND`.
5. **Polynesian Soundscape Synthesizer**: Native audio engine generating calming tropical ocean waves, gentle island breezes, and acoustic tones.
6. **Launcher App Shortcuts**: Direct home-screen long-press shortcuts for Itinerary Planner, Whale Sanctuary, and Phrasebook.
7. **Tactile Haptic Feedback**: Refined tactile response during itinerary generation and tab navigation.

## Deferred (file tickets)
- Interactive Offline Vector Map with GPS markers (needs vector tile engine).
- Home Screen Glance Widget (Tonga Local Time, Live Weather, and Whale Season Tracker).
- Itinerary Photo Attachments (attaching device camera/gallery photos to saved trip days).

## Rejected
- Notifications: Travel guide has no time-sensitive alerts or push notifications.
- BiometricPrompt: No sensitive personal credentials, financial transactions, or health records.
- GPS Location tracking: Unnecessary for a curated travel guide; avoids intrusive permission prompts.

## Permissions requested in v1
- `android.permission.INTERNET` (optional live weather & currency refresh; app is 100% functional without it)
- `android.permission.ACCESS_NETWORK_STATE` (detect connectivity to smoothly toggle between live and cached weather/rates)
- `android.permission.VIBRATE` (tactile haptic feedback; normal permission, no user prompt required)
- **Zero dangerous runtime permissions!**
