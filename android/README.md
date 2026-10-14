# Kingdom of Tonga Tourism for Android

Native Android client for the Kingdom of Tonga Tourism guide (Kotlin K2, Jetpack Compose, Material 3, Cedar Design System). Designed specifically for Cody's **Samsung Galaxy Z Fold 8 Ultra** and **Galaxy S26+**. Sideloaded via USB and never published to the Play Store.

See [`NATIVE_EVALUATION.md`](./NATIVE_EVALUATION.md) for native capability evaluations, trade-offs, and architecture decisions.

---

## 🌴 Key Features

- **Discover**: Real-time Tonga local time clock (`UTC+13`), live Nuku'alofa weather metrics via Open-Meteo with offline fallbacks, live currency conversion rates, and Kingdom statistics.
- **Islands Explorer**:
  - Filterable cards across all 4 major island archipelagos (*Tongatapu*, *Vava'u*, *Ha'apai*, *'Eua*).
  - High-resolution bundled imagery, flight times, capital hubs, airport logistics, and landmark guides.
  - Interactive detail bottom sheet with dual-photo views and direct itinerary links.
- **Culture & Heritage**:
  - Comprehensive guide covering the sacred *Faikava* (Kava ceremony) protocols, *Ngatu* barkcloth and *Ta'ovala* fine mats, *Lakalaka* and *Kailao* warrior dances, and authentic *'Umu* earth oven gastronomy ('Ota Ika, Lu Pulu, Sipi Tavahi, Topai).
  - Dedicated visitor etiquette and respect rules.
- **Humpback Whale Sanctuary**:
  - Month-by-month migration calendar (June to November) tracking arrival, courtship, and peak mother-calf swimming seasons.
  - Procedural acoustic whale song synthesizer reproducing ocean acoustic calls via low-latency `AudioTrack`.
  - Swimmer safety rules, minimum distance mandates, and marine sanctuary facts.
- **Custom Itinerary Planner**:
  - Interactive multi-day schedule generator configured by duration (3, 5, 7, 10, 14 days), island selection, and travel style.
  - Local Room database persistence for offline trip storage.
  - Native Android Sharesheet (`ACTION_SEND`) to share markdown trip itineraries via messaging or email.
- **Travel Essentials**:
  - Tongan Pa'anga (`TOP`) currency converter with daily budget tiers (Backpacker, Island Explorer, Royal Sanctuary).
  - Visa entry rules for 30-day visitor visas.
  - Constitutionally protected Sunday sacred Sabbath observance laws.
  - Faka-Tonga phrasebook with phonetic pronunciations and one-tap sharing.
  - Emergency contacts quick-dial (Vaiola Hospital, Police, Search & Rescue) launching Android's system dialer without requiring intrusive phone permissions.
- **Cedar Design Language**:
  - Deep Cedar Spruce (`#1F513F`), ecru canvas, Source Serif 4 headings, Inter body, and JetBrains Mono metrics.
  - Universal light/dark theme toggle accessible in the top app bar of every screen.
- **Foldable Content-First (Galaxy Z Fold 8 Ultra)**:
  - Cover screen (360×840dp): Single pane with bottom navigation.
  - Inner unfolded screen (752×834dp): Slim ~72dp navigation rail dedicating ≥85% of screen width to fluid multi-column island cards, side-by-side culture guides, and split-pane itinerary planning.

---

## 🛠️ Build & Verification

```bash
cd android

# 1. Run unit tests & data parity verification
./gradlew testDebugUnitTest

# 2. Run Android lint checks
./gradlew lintDebug

# 3. Assemble Debug APK
./gradlew assembleDebug

# 4. Assemble Release APK (R8 minified & shrunk)
./gradlew assembleRelease
```

---

## 📱 Sideloading onto Phones

```bash
# Verify device connection
adb devices -l

# Install release build via homelab helper
python3 ~/dev/repos/skills/skills/create-android-app/scripts/install_apk.py tongan-tourism <version>

# Or install local debug build directly
python3 ~/dev/repos/skills/skills/create-android-app/scripts/install_apk.py --apk android/app/build/outputs/apk/debug/app-debug.apk
```
