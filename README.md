# 👑 Kingdom of Tonga Tourism Application

A modern, high-performance web application showcasing the Kingdom of Tonga — exploring its four main archipelagos (*Tongatapu*, *Vava'u*, *Ha'apai*, and *'Eua*), rich 3,000-year Polynesian cultural heritage, world-renowned humpback whale sanctuary, interactive itinerary planner, and practical travel essentials.

---

## 🎯 What It Is Used For

This application serves as an interactive travel and destination guide for visitors exploring Tonga. Key features include:

- **Islands Explorer**: Interactive SVG map and filterable cards for Tonga's main island groups, highlights, airport logistics, and natural landmarks (such as the *Mapu 'a Vaea Blowholes* and *Ha'amonga 'a Maui Trilithon*).
- **Culture & Heritage**: Comprehensive guides on the sacred *Faikava* (Kava ceremony), *Ngatu* barkcloth crafting, *Ta'ovala* fine mats, traditional dances (*Lakalaka* and *Kailao*), and local gastronomy (*'Ota Ika*).
- **Whale Swim Sanctuary**: June–November humpback whale migration calendar, ethical swimming rules, and sanctuary marine facts.
- **Interactive Itinerary Planner**: Custom trip schedule generator based on stay duration, island selection, and travel style, with local storage itinerary saving.
- **Travel Essentials**: Live Tongan Pa'anga (`TOP`) currency converter with daily travel budget tiers, visa entry rules, Sunday sacred laws guide, and a Faka-Tonga phrasebook with written phonetic pronunciation guides.
- **Real-Time Data**: Live local time clock (`UTC+13`), live weather updates for Nuku'alofa, and live market exchange rates.

---

## 🏗️ Application Architecture

The project is structured as a zero-framework, lightweight Single-Page Application (SPA) located in the [`app`](app) directory.

```
tongan-tourism/
├── app/
│   ├── index.html            # Main application structure & semantic layout
│   ├── package.json          # Node dependencies and Vite scripts
│   ├── vite.config.js        # Vite bundler configuration
│   ├── public/
│   │   └── images/           # High-resolution asset imagery
│   └── src/
│       ├── main.js           # Reactive view router, modal controllers & API handlers
│       ├── style.css         # Glassmorphism design system, CSS variables & theme rules
│       ├── data/
│       │   └── tongaData.js  # Structured dataset (islands, culture, whales, phrases)
│       └── utils/
│           └── audio.js      # Web Audio API acoustic soundscape synthesizer
├── Dockerfile                # Multi-stage container build definition
├── .dockerignore              # Docker build context exclusions
└── README.md                 # Project documentation
```

### Technical Stack & Key Technologies

1. **Frontend Core**: Vanilla HTML5, CSS3, and ES6+ JavaScript.
2. **Bundler & Dev Server**: [Vite](https://vitejs.dev/) for fast dev serving and production bundling.
3. **Design System**:
   - Modern Glassmorphism layout with customizable light/dark themes (default: **Dark Mode**).
   - High-contrast, accessible typography using Google Fonts (*Cinzel*, *Outfit*, *Plus Jakarta Sans*).
   - Top-layer dialog entry/exit animations using CSS `@starting-style`, `transition-behavior: allow-discrete`, and `overlay`.
   - Native View Transitions API for smooth view switches.
4. **Keyless Dynamic APIs**:
   - **Live Weather**: [Open-Meteo API](https://open-meteo.com/) for current Nuku'alofa temperature and wind metrics.
   - **Live Currency**: [ExchangeRate-API](https://www.exchangerate-api.com/) for real-time TOP exchange rates vs. USD, AUD, NZD, EUR, and GBP.
5. **Containerization**: Multi-stage `Dockerfile` using `node:20-alpine` to build production static assets, served via `nginx:1.25-alpine`.

---

## 💻 How to Run the App

### Option 1: Local Development Server

Make sure you have [Node.js](https://nodejs.org/) (v18+) installed.

```bash
# 1. Navigate to the app directory
cd app

# 2. Install dependencies (first time only)
npm install

# 3. Start the local Vite development server
npm run dev
```

Open your browser and navigate to **`http://localhost:3000/`**.

---

### Option 2: Run with Docker

Ensure [Docker](https://www.docker.com/) is installed and running on your machine.

```bash
# 1. Build the Docker container image from the repository root
docker build -t tongan-tourism:latest .

# 2. Run the container mapping HTTP port 8080 to container port 80
docker run -d -p 8080:80 --name tongan-tourism-app tongan-tourism:latest
```

Open your browser and navigate to **`http://localhost:8080/`**.

To stop and remove the running container:
```bash
docker stop tongan-tourism-app
docker rm tongan-tourism-app
```

---

## 📦 How to Build the App

### Production Bundle Build (Vite)

To compile the optimized static assets for production deployment:

```bash
cd app
npm run build
```

This compiles the static files into `app/dist/` (`index.html`, minified CSS, bundled JS, and assets).

To test the compiled production build locally:
```bash
npm run preview
```

### Docker Production Image Build

To build the production container image:

```bash
docker build -t tongan-tourism:latest .
```

---

## 📜 License & Copyright

© 2026 **Cody Uhi**. All rights reserved.
