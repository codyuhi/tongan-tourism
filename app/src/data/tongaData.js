// Rich Data Repository for Tonga Tourism Application

export const TONGA_DATA = {
  stats: {
    islandsCount: 171,
    inhabitedCount: 45,
    whalesAnnually: "1,000+",
    historyYears: "3,000+",
    timeZone: "UTC+13 (TOT)",
    capital: "Nuku'alofa",
    currency: "Tongan Pa'anga (TOP / $T)"
  },

  islands: [
    {
      id: "tongatapu",
      name: "Tongatapu",
      tagline: "The Royal Seat & Ancient Gateway",
      group: "Southern Group",
      capital: "Nuku'alofa",
      airport: "Fua'amotu International (TBU)",
      image: "/images/hero-lagoon.jpg",
      secondaryImage: "/images/blowholes.jpg",
      description: "Tongatapu is the main island of the Kingdom of Tonga and home to the royal family, ancient megaliths, and coastline blowholes. It blends vibrant Polynesian urban life with deep historical monuments.",
      highlights: [
        {
          title: "Mapu 'a Vaea Blowholes",
          desc: "Natural limestone blowholes stretching over 5km along the southern coast, blasting ocean spray up to 30 meters high."
        },
        {
          title: "Ha'amonga 'a Maui Trilithon",
          desc: "Ancient 13th-century stone archway often called the 'Stonehenge of the Pacific', aligned with solstices."
        },
        {
          title: "Anahulu Cave & Underground Pool",
          desc: "A massive freshwater limestone cave filled with glistening stalactites where visitors can swim in cool natural pools."
        },
        {
          title: "Royal Palace & Grounds",
          desc: "The iconic Victorian wooden royal palace built in 1867 along the Nuku'alofa waterfront."
        }
      ],
      bestFor: ["Culture", "History", "Family", "Sightseeing"],
      flightTimeFromCapital: "0 mins (Main Hub)",
      coordinates: { x: 45, y: 78 }
    },
    {
      id: "vavau",
      name: "Vava'u",
      tagline: "Sailing Sanctuary & Whale Capital",
      group: "Northern Group",
      capital: "Neiafu",
      airport: "Lupepau'u Airport (VAV)",
      image: "/images/vavau.jpg",
      secondaryImage: "/images/whales.jpg",
      description: "A tropical paradise of 34 jungle-clad coral islands encircling a maze of deep waterways, quiet fjords, and world-class sailing anchorage. The prime hub for swimming with migrating humpback whales.",
      highlights: [
        {
          title: "Swallow's Cave & Mariner's Cave",
          desc: "Sea-accessible caves illuminated by sapphire ocean reflection; Mariner's Cave requires an underwater dive entrance."
        },
        {
          title: "Port of Refuge",
          desc: "Considered one of the safest and most picturesque natural yacht harbors in the entire South Pacific."
        },
        {
          title: "Humpback Whale Swimming",
          desc: "Crystal-clear sheltered waters provide optimal conditions for swimming alongside maternal humpback whales and calves."
        },
        {
          title: "Mount 'Talau National Park",
          desc: "Flat-topped peak overlooking Neiafu Harbor offering panoramic views and endemic wildlife sightings."
        }
      ],
      bestFor: ["Whale Swimming", "Sailing", "Diving", "Romance"],
      flightTimeFromCapital: "50 mins flight from TBU",
      coordinates: { x: 62, y: 32 }
    },
    {
      id: "haapai",
      name: "Ha'apai",
      tagline: "Untouched Coral Atolls & Tropical Solitude",
      group: "Central Group",
      capital: "Pangai",
      airport: "Salote Pilolevu Airport (HPA)",
      image: "/images/haapai.jpg",
      secondaryImage: "/images/hero-lagoon.jpg",
      description: "Low-lying coral islands and uninhabited sandbars with powdery white sand beaches and fringing reefs. Ha'apai is where Captain Cook dubbed Tonga 'The Friendly Islands' in 1777.",
      highlights: [
        {
          title: "Foa & Lifuka Island Sandbars",
          desc: "Endless pristine white sand beaches where turquoise waters kiss calm barrier reefs."
        },
        {
          title: "Tofua Active Volcano View",
          desc: "Site of the infamous 1789 Mutiny on the Bounty, dominated by an active volcanic crater."
        },
        {
          title: "Uncrowded Coral Reef Diving",
          desc: "Submerged pinnacles, swim-throughs, and untouched coral gardens with green sea turtles."
        },
        {
          title: "Captain Cook Landing Site",
          desc: "Historical marker in Pangai commemorating the British explorer's feast in 1777."
        }
      ],
      bestFor: ["Eco-Luxury", "Beach Solitude", "Kitesurfing", "Snorkeling"],
      flightTimeFromCapital: "35 mins flight from TBU",
      coordinates: { x: 52, y: 55 }
    },
    {
      id: "eua",
      name: "'Eua",
      tagline: "Ancient Rainforests & Eco-Adventure",
      group: "Southern Group",
      capital: "'Ohonua",
      airport: "Kaufana Airport (EUA)",
      image: "/images/eua.jpg",
      secondaryImage: "/images/kava.jpg",
      description: "Geologically the oldest island in Tonga (40 million years old), featuring high sea cliffs, ancient tropical rainforests, sinkholes, and wild horses roaming lush plateaus.",
      highlights: [
        {
          title: "'Eua National Park & Rainforest Treks",
          desc: "Hiking trails winding through virgin rainforest, giant strangler fig trees, and koki red-breasted parrots."
        },
        {
          title: "Fangatave Beach & Cave Systems",
          desc: "Remote limestone cliff caves accessed through rainforest descents opening to secluded ocean coves."
        },
        {
          title: "Lokupo & Cliff Overlooks",
          desc: "Vertical ocean drop-offs over 200 meters high offering spectacular whale watching from land."
        },
        {
          title: "'Hafu Waterfall & Natural Basins",
          desc: "Refreshingly cool mountain streams cascading through moss-covered basalt rocks."
        }
      ],
      bestFor: ["Hiking", "Eco-Trekking", "Caving", "Birdwatching"],
      flightTimeFromCapital: "8 mins flight (One of world's shortest!) or 2hr ferry",
      coordinates: { x: 48, y: 85 }
    }
  ],

  culture: [
    {
      id: "faikava",
      title: "Faikava (The Kava Ceremony)",
      tagline: "The sacred potion of hospitality, peace & connection",
      image: "/images/kava.jpg",
      summary: "In Tongan culture, Kava (made from crushed roots of Piper methysticum) is far more than a drink—it is the cornerstone of social cohesion, royal protocol, and community fellowship.",
      details: [
        "Served in a carved wooden bowl called a Tanoa using coconut shells (ipukava).",
        "Participant hierarchy follows traditional rules of respect and seat positioning.",
        "Promotes relaxed conversation, acoustic stringband music, and storytelling late into the night."
      ],
      etiquetteTips: [
        "Cross your legs comfortably on the woven mat.",
        "Accept the coconut shell with both hands and say 'Mālō'.",
        "Drink the kava in one smooth sip, then return the shell with a warm smile."
      ]
    },
    {
      id: "ngatu",
      title: "Ngatu (Barkcloth) & Ta'ovala",
      tagline: "Woven identity, royal reverence & traditional attire",
      image: "/images/kava.jpg",
      summary: "Tongans take immense pride in traditional handcrafts. The Ta'ovala is a woven mat worn around the waist by both men and women as a symbol of respect, akin to wearing a suit or formal dress.",
      details: [
        "Ngatu is crafted from the inner bark of the paper mulberry tree, beaten with wooden mallets (ike).",
        "Painted with natural earth dyes using traditional Tongan geometric motifs.",
        "Exchanged as valuable family heirlooms during weddings, coronations, and milestone celebrations."
      ],
      etiquetteTips: [
        "Always wear a Ta'ovala or neat attire when attending church or formal Tongan functions.",
        "Treat Ngatu barkcloth gifts with immense respect as they carry ancestral blessing."
      ]
    },
    {
      id: "dance",
      title: "Lakalaka & Kailao Dances",
      tagline: "Poetry in motion & thunderous Polynesian warrior rhythm",
      image: "/images/hero-lagoon.jpg",
      summary: "Recognized by UNESCO as a Masterpiece of the Intangible Heritage of Humanity, the Lakalaka is Tonga's national dance, blending choral harmony with intricate hand gestures.",
      details: [
        "Lakalaka combines hundreds of dancers standing in rows reciting epic historical poetry.",
        "Men perform strong, energetic actions while women perform graceful hand and head movements (haka).",
        "Kailao is a vibrant war dance accompanied by heavy wooden club rhythms and drum beats."
      ],
      etiquetteTips: [
        "Clapping in rhythm (pāpā) during performances is encouraged.",
        "Audience members may place small money gifts (fakapale) on favored performers to show admiration."
      ]
    },
    {
      id: "cuisine",
      title: "Tongan Feast & Umu Cooking",
      tagline: "Fresh ocean yields & earth oven delicacies",
      image: "/images/ota-ika.jpg",
      summary: "Tongan gastronomy revolves around fresh ocean catch, coconut cream, taro, yam, and suckling pig cooked to perfection in an underground earth oven ('umu).",
      dishes: [
        {
          name: "'Ota Ika",
          desc: "Fresh raw tuna or snapper marinated in lime juice, diced cucumber, tomatoes, spring onions, and rich coconut cream."
        },
        {
          name: "Lu Pulu",
          desc: "Tender corned beef or lamb wrapped in young taro leaves with coconut cream, baked in the 'umu."
        },
        {
          name: "Sipi Tavahi",
          desc: "Traditional slow-roasted lamb chops seasoned with ginger and sweet coconut glaze."
        },
        {
          name: "Topai",
          desc: "Sweet coconut dumplings boiled in coconut syrup, a favorite island dessert."
        }
      ]
    }
  ],

  whales: {
    heroImage: "/images/whales.jpg",
    season: [
      { month: "June", status: "Arrival", description: "First humpback pods arrive from Antarctic feeding grounds to warm Tongan lagoons." },
      { month: "July", status: "Mating & Birth", description: "Courtship pods assemble; pregnant mothers give birth in sheltered waters." },
      { month: "August", status: "Peak Season", description: "Optimal conditions for swimming with curious newborn calves and protective mothers." },
      { month: "September", status: "Peak Season", description: "High whale activity; underwater male whale songs echo through the deep bays." },
      { month: "October", status: "Active Swimming", description: "Calves grow stronger and display breach and tail-slap behaviors." },
      { month: "November", status: "Departure", description: "Whales commence their southward migration back to southern polar oceans." }
    ],
    rules: [
      "Maximum of 4 swimmers plus 1 certified licensed local guide in the water at any time.",
      "Maintain a respectful distance (minimum 5 meters from whales, 10 meters from mothers with calves).",
      "No touching, chasing, or using underwater flash photography/scuba gear.",
      "Enter the water quietly without splashing to ensure calm interaction."
    ],
    facts: [
      "Tonga declared its entire EEZ ocean area a Sanctuary for Whales in 1979.",
      "Humpback whales travel over 6,000 km from Antarctica to breed in Tonga.",
      "Calves are born weighing around 1 to 1.5 tons and gain up to 45kg of weight per day from milk!"
    ]
  },

  phrases: [
    { tongan: "Mālō e lelei", english: "Hello / Good day", usage: "Universal greeting", audioKey: "malo_e_lelei" },
    { tongan: "Mālō 'aupito", english: "Thank you very much", usage: "Polite gratitude", audioKey: "malo_aupito" },
    { tongan: "'Ofa atu", english: "With love / Warm regards", usage: "Farewell or sign-off", audioKey: "ofa_atu" },
    { tongan: "Fēfe hake?", english: "How are you?", usage: "Casual check-in", audioKey: "fefe_hake" },
    { tongan: "Sai pē", english: "I am fine / All good", usage: "Response to Fēfe hake", audioKey: "sai_pe" },
    { tongan: "Tāufaka'ofa", english: "Beautiful / Magnificent", usage: "Praising sights & music", audioKey: "taufakao fa" },
    { tongan: "Nofo ā", english: "Goodbye (to one staying)", usage: "Leaving someone", audioKey: "nofo_a" },
    { tongan: "'Alu ā", english: "Goodbye (to one leaving)", usage: "Said by host to guest", audioKey: "alu_a" }
  ],

  currency: {
    baseCurrency: "TOP",
    symbol: "$T",
    rates: {
      USD: 0.42,
      AUD: 0.64,
      NZD: 0.70,
      EUR: 0.39,
      GBP: 0.33
    },
    budgets: {
      backpacker: { label: "Budget Backpacker", costTOP: 130, desc: "Guesthouses, local food stalls, public buses & shared ferry routes." },
      midrange: { label: "Island Explorer", costTOP: 320, desc: "Beachfront bungalows, local tours, dining out & inter-island flights." },
      luxury: { label: "Royal Sanctuary", costTOP: 750, desc: "Private island resorts, private yacht charters & guided whale swims." }
    }
  },

  visaInfo: [
    { region: "Passport Holders (US, AU, NZ, EU, UK, CA, JP)", rule: "Free 30-day Visitor Visa granted upon arrival.", requirements: "Valid passport (6+ months remaining), return ticket, proof of sufficient funds." },
    { region: "Pacific Island Nations (PIF)", rule: "Free 30-day Visa on Arrival.", requirements: "Onward ticket & passport." },
    { region: "Other Countries", rule: "Requires pre-arranged entry permit from Tongan Immigration before arrival.", requirements: "Submit online application 1 month prior." }
  ],

  itineraryPresets: [
    {
      id: "classic-7day",
      title: "The Royal & Marine Sanctuary Odyssey",
      duration: "7 Days",
      vibe: "Balanced Explorer",
      islands: ["Tongatapu", "Vava'u"],
      days: [
        { day: 1, island: "Tongatapu", title: "Arrival & Royal Capital", desc: "Land at TBU, check in at Nuku'alofa waterfront hotel, visit Talamahu Market and Royal Palace." },
        { day: 2, island: "Tongatapu", title: "Megaliths & Blowholes", desc: "Full day tour of Mapu 'a Vaea blowholes, Anahulu freshwater cave, and ancient Ha'amonga 'a Maui trilithon." },
        { day: 3, island: "Vava'u", title: "Flight to Sailing Capital", desc: "Short scenic flight to Neiafu, Vava'u. Sunset dinner overlooking Port of Refuge harbor." },
        { day: 4, island: "Vava'u", title: "Whale Swim Expedition", desc: "Guided ocean swim alongside maternal humpback whales in sheltered sapphire bays." },
        { day: 5, island: "Vava'u", title: "Caves & Island Catamaran", desc: "Explore Swallow's Cave by kayak or boat, snorkel Port of Refuge coral gardens." },
        { day: 6, island: "Tongatapu", title: "Return & Traditional Faikava", desc: "Fly back to Tongatapu, attend an evening cultural show & Faikava ceremony with 'Ota Ika feast." },
        { day: 7, island: "Tongatapu", title: "Souvenirs & Departure", desc: "Shop for hand-carved bone pendants and fine mats, departure from Fua'amotu Airport." }
      ]
    },
    {
      id: "eco-adventure-6day",
      title: "'Eua Rainforest & Ha'apai Solitude",
      duration: "6 Days",
      vibe: "Adventure & Hiking",
      islands: ["'Eua", "Ha'apai"],
      days: [
        { day: 1, island: "'Eua", title: "Arrival in Ancient Rainforest", desc: "Fly or ferry to 'Eua, check into eco-lodge, sunset walk to Lokupo cliff outlook." },
        { day: 2, island: "'Eua", title: "National Park & Cave Descent", desc: "Guided hike through ancient strangler fig groves to Fangatave beach caves." },
        { day: 3, island: "Ha'apai", title: "Transfer to Uninhabited Atolls", desc: "Flight connection to Pangai, Ha'apai. Settle into beachfront fale." },
        { day: 4, island: "Ha'apai", title: "Unspoiled Coral Reef Diving", desc: "Dive untouched submerged pinnacles and spot sea turtles in crystal clear waters." },
        { day: 5, island: "Ha'apai", title: "Sandbar Picnic & Kitesurfing", desc: "Boat drop-off at an uninhabited sandbar for a private beach picnic." },
        { day: 6, island: "Tongatapu", title: "Return to Main Hub", desc: "Fly back to Tongatapu for final connection home." }
      ]
    }
  ]
};
