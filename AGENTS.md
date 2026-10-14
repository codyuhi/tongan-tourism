# AGENTS.md — AI Agent Guide for Kingdom of Tonga Tourism

## Project Overview
Kingdom of Tonga Tourism is a rich South Pacific travel guide and marine sanctuary application consisting of:
- `app/`: Modern single-page web application (Vanilla HTML5/ES6, Vite, Glassmorphism & Cedar design tokens, Web Audio acoustic synthesizer).
- `android/`: Native Android client (Kotlin K2, Jetpack Compose, Material 3, Cedar design system, Room database persistence, procedural acoustic whale synthesizer, adaptive foldable layouts).

## Android Client (`android/`)
- **Namespace & Application ID**: `online.codyuhi.tongantourism`
- **Architecture**: Single-activity MVVM + UDF pattern, manual DI via `AppContainer`, Navigation Compose with type-safe `@Serializable` routes, Cedar theme tokens.
- **Form Factors**:
  - Compact (360×840dp, Galaxy Z Fold cover screen and Galaxy S-series phones): Bottom navigation bar, full-width cards.
  - Medium (752×834dp, Galaxy Z Fold inner unfolded screen): Foldable Content-First (≥85% width for primary content, slim 72dp navigation rail, multi-column island grid).
- **Offline & Connectivity**:
  - 100% offline-first architecture with bundled dataset and Room database (`tongan_tourism.db`) for saved itineraries.
  - Keyless background refresh for live weather (Open-Meteo) and currency rates (ExchangeRate-API) with instant offline fallbacks.
  - Zero required runtime permissions (Quick-dial uses system `Intent.ACTION_DIAL`).
- **Local Build & Test**:
  ```bash
  cd android
  ./gradlew lintDebug testDebugUnitTest
  ./gradlew assembleDebug
  ./gradlew assembleRelease
  ```
- **CI & Release**:
  - Built by Argo Workflows template `android-build-apk` in namespace `argo-workflows`.
  - Pushed to Harbor as `harbor.minipc.local/apps/tongan-tourism-android:<version>`.
  - Installed via USB: `python3 ~/dev/repos/skills/skills/create-android-app/scripts/install_apk.py tongan-tourism <version>`.
