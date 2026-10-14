package online.codyuhi.tongantourism.data.repository

import android.content.Context
import online.codyuhi.tongantourism.R
import online.codyuhi.tongantourism.domain.BudgetTier
import online.codyuhi.tongantourism.domain.CultureItem
import online.codyuhi.tongantourism.domain.Dish
import online.codyuhi.tongantourism.domain.Highlight
import online.codyuhi.tongantourism.domain.Island
import online.codyuhi.tongantourism.domain.KingdomStats
import online.codyuhi.tongantourism.domain.Phrase
import online.codyuhi.tongantourism.domain.VisaInfo
import online.codyuhi.tongantourism.domain.WhaleData
import online.codyuhi.tongantourism.domain.WhaleSeason

class TongaDataRepository(private val context: Context? = null) {

    val stats: KingdomStats = KingdomStats()

    val islands: List<Island> = listOf(
        Island(
            id = "tongatapu",
            name = "Tongatapu",
            tagline = "The Royal Seat & Ancient Gateway",
            group = "Southern Group",
            capital = "Nuku'alofa",
            airport = "Fua'amotu International (TBU)",
            imageRes = R.drawable.img_hero_lagoon,
            secondaryImageRes = R.drawable.img_blowholes,
            description = "Tongatapu is the main island of the Kingdom of Tonga and home to the royal family, ancient megaliths, and coastline blowholes. It blends vibrant Polynesian urban life with deep historical monuments.",
            highlights = listOf(
                Highlight(
                    title = "Mapu 'a Vaea Blowholes",
                    desc = "Natural limestone blowholes stretching over 5km along the southern coast, blasting ocean spray up to 30 meters high."
                ),
                Highlight(
                    title = "Ha'amonga 'a Maui Trilithon",
                    desc = "Ancient 13th-century stone archway often called the 'Stonehenge of the Pacific', aligned with solstices."
                ),
                Highlight(
                    title = "Anahulu Cave & Underground Pool",
                    desc = "A massive freshwater limestone cave filled with glistening stalactites where visitors can swim in cool natural pools."
                ),
                Highlight(
                    title = "Royal Palace & Grounds",
                    desc = "The iconic Victorian wooden royal palace built in 1867 along the Nuku'alofa waterfront."
                )
            ),
            bestFor = listOf("Culture", "History", "Family", "Sightseeing"),
            flightTimeFromCapital = "0 mins (Main Hub)",
            coordX = 45,
            coordY = 78
        ),
        Island(
            id = "vavau",
            name = "Vava'u",
            tagline = "Sailing Sanctuary & Whale Capital",
            group = "Northern Group",
            capital = "Neiafu",
            airport = "Lupepau'u Airport (VAV)",
            imageRes = R.drawable.img_vavau,
            secondaryImageRes = R.drawable.img_whales,
            description = "A tropical paradise of 34 jungle-clad coral islands encircling a maze of deep waterways, quiet fjords, and world-class sailing anchorage. The prime hub for swimming with migrating humpback whales.",
            highlights = listOf(
                Highlight(
                    title = "Swallow's Cave & Mariner's Cave",
                    desc = "Sea-accessible caves illuminated by sapphire ocean reflection; Mariner's Cave requires an underwater dive entrance."
                ),
                Highlight(
                    title = "Port of Refuge",
                    desc = "Considered one of the safest and most picturesque natural yacht harbors in the entire South Pacific."
                ),
                Highlight(
                    title = "Humpback Whale Swimming",
                    desc = "Crystal-clear sheltered waters provide optimal conditions for swimming alongside maternal humpback whales and calves."
                ),
                Highlight(
                    title = "Mount 'Talau National Park",
                    desc = "Flat-topped peak overlooking Neiafu Harbor offering panoramic views and endemic wildlife sightings."
                )
            ),
            bestFor = listOf("Whale Swimming", "Sailing", "Diving", "Romance"),
            flightTimeFromCapital = "50 mins flight from TBU",
            coordX = 62,
            coordY = 32
        ),
        Island(
            id = "haapai",
            name = "Ha'apai",
            tagline = "Untouched Coral Atolls & Tropical Solitude",
            group = "Central Group",
            capital = "Pangai",
            airport = "Salote Pilolevu Airport (HPA)",
            imageRes = R.drawable.img_haapai,
            secondaryImageRes = R.drawable.img_hero_lagoon,
            description = "Low-lying coral islands and uninhabited sandbars with powdery white sand beaches and fringing reefs. Ha'apai is where Captain Cook dubbed Tonga 'The Friendly Islands' in 1777.",
            highlights = listOf(
                Highlight(
                    title = "Foa & Lifuka Island Sandbars",
                    desc = "Endless pristine white sand beaches where turquoise waters kiss calm barrier reefs."
                ),
                Highlight(
                    title = "Tofua Active Volcano View",
                    desc = "Site of the infamous 1789 Mutiny on the Bounty, dominated by an active volcanic crater."
                ),
                Highlight(
                    title = "Uncrowded Coral Reef Diving",
                    desc = "Submerged pinnacles, swim-throughs, and untouched coral gardens with green sea turtles."
                ),
                Highlight(
                    title = "Captain Cook Landing Site",
                    desc = "Historical marker in Pangai commemorating the British explorer's feast in 1777."
                )
            ),
            bestFor = listOf("Eco-Luxury", "Beach Solitude", "Kitesurfing", "Snorkeling"),
            flightTimeFromCapital = "35 mins flight from TBU",
            coordX = 52,
            coordY = 55
        ),
        Island(
            id = "eua",
            name = "'Eua",
            tagline = "Ancient Rainforests & Eco-Adventure",
            group = "Southern Group",
            capital = "'Ohonua",
            airport = "Kaufana Airport (EUA)",
            imageRes = R.drawable.img_eua,
            secondaryImageRes = R.drawable.img_kava,
            description = "Geologically the oldest island in Tonga (40 million years old), featuring high sea cliffs, ancient tropical rainforests, sinkholes, and wild horses roaming lush plateaus.",
            highlights = listOf(
                Highlight(
                    title = "'Eua National Park & Rainforest Treks",
                    desc = "Hiking trails winding through virgin rainforest, giant strangler fig trees, and koki red-breasted parrots."
                ),
                Highlight(
                    title = "Fangatave Beach & Cave Systems",
                    desc = "Remote limestone cliff caves accessed through rainforest descents opening to secluded ocean coves."
                ),
                Highlight(
                    title = "Lokupo & Cliff Overlooks",
                    desc = "Vertical ocean drop-offs over 200 meters high offering spectacular whale watching from land."
                ),
                Highlight(
                    title = "'Hafu Waterfall & Natural Basins",
                    desc = "Refreshingly cool mountain streams cascading through moss-covered basalt rocks."
                )
            ),
            bestFor = listOf("Hiking", "Eco-Trekking", "Caving", "Birdwatching"),
            flightTimeFromCapital = "8 mins flight (One of world's shortest!) or 2hr ferry",
            coordX = 48,
            coordY = 85
        )
    )

    val culture: List<CultureItem> = listOf(
        CultureItem(
            id = "faikava",
            title = "Faikava (The Kava Ceremony)",
            tagline = "The sacred potion of hospitality, peace & connection",
            imageRes = R.drawable.img_kava,
            summary = "In Tongan culture, Kava (made from crushed roots of Piper methysticum) is far more than a drink—it is the cornerstone of social cohesion, royal protocol, and community fellowship.",
            details = listOf(
                "Served in a carved wooden bowl called a Tanoa using coconut shells (ipukava).",
                "Participant hierarchy follows traditional rules of respect and seat positioning.",
                "Promotes relaxed conversation, acoustic stringband music, and storytelling late into the night."
            ),
            etiquetteTips = listOf(
                "Cross your legs comfortably on the woven mat.",
                "Accept the coconut shell with both hands and say 'Mālō'.",
                "Drink the kava in one smooth sip, then return the shell with a warm smile."
            )
        ),
        CultureItem(
            id = "ngatu",
            title = "Ngatu (Barkcloth) & Ta'ovala",
            tagline = "Woven identity, royal reverence & traditional attire",
            imageRes = R.drawable.img_kava,
            summary = "Tongans take immense pride in traditional handcrafts. The Ta'ovala is a woven mat worn around the waist by both men and women as a symbol of respect, akin to wearing a suit or formal dress.",
            details = listOf(
                "Ngatu is crafted from the inner bark of the paper mulberry tree, beaten with wooden mallets (ike).",
                "Painted with natural earth dyes using traditional Tongan geometric motifs.",
                "Exchanged as valuable family heirlooms during weddings, coronations, and milestone celebrations."
            ),
            etiquetteTips = listOf(
                "Always wear a Ta'ovala or neat attire when attending church or formal Tongan functions.",
                "Treat Ngatu barkcloth gifts with immense respect as they carry ancestral blessing."
            )
        ),
        CultureItem(
            id = "dance",
            title = "Lakalaka & Kailao Dances",
            tagline = "Poetry in motion & thunderous Polynesian warrior rhythm",
            imageRes = R.drawable.img_hero_lagoon,
            summary = "Recognized by UNESCO as a Masterpiece of the Intangible Heritage of Humanity, the Lakalaka is Tonga's national dance, blending choral harmony with intricate hand gestures.",
            details = listOf(
                "Lakalaka combines hundreds of dancers standing in rows reciting epic historical poetry.",
                "Men perform strong, energetic actions while women perform graceful hand and head movements (haka).",
                "Kailao is a vibrant war dance accompanied by heavy wooden club rhythms and drum beats."
            ),
            etiquetteTips = listOf(
                "Clapping in rhythm (pāpā) during performances is encouraged.",
                "Audience members may place small money gifts (fakapale) on favored performers to show admiration."
            )
        ),
        CultureItem(
            id = "cuisine",
            title = "Tongan Feast & Umu Cooking",
            tagline = "Fresh ocean yields & earth oven delicacies",
            imageRes = R.drawable.img_ota_ika,
            summary = "Tongan gastronomy revolves around fresh ocean catch, coconut cream, taro, yam, and suckling pig cooked to perfection in an underground earth oven ('umu).",
            details = listOf(
                "Feasts are prepared for Sundays, weddings, and village gatherings.",
                "The 'umu uses heated volcanic basalt stones covered in banana leaves.",
                "Meals are shared communally in true Polynesian hospitality."
            ),
            dishes = listOf(
                Dish(
                    name = "'Ota Ika",
                    desc = "Fresh raw tuna or snapper marinated in lime juice, diced cucumber, tomatoes, spring onions, and rich coconut cream."
                ),
                Dish(
                    name = "Lu Pulu",
                    desc = "Tender corned beef or lamb wrapped in young taro leaves with coconut cream, baked in the 'umu."
                ),
                Dish(
                    name = "Sipi Tavahi",
                    desc = "Traditional slow-roasted lamb chops seasoned with ginger and sweet coconut glaze."
                ),
                Dish(
                    name = "Topai",
                    desc = "Sweet coconut dumplings boiled in coconut syrup, a favorite island dessert."
                )
            )
        )
    )

    val whales: WhaleData = WhaleData(
        heroImageRes = R.drawable.img_whales,
        season = listOf(
            WhaleSeason(
                month = "June",
                status = "Arrival",
                description = "First humpback pods arrive from Antarctic feeding grounds to warm Tongan lagoons."
            ),
            WhaleSeason(
                month = "July",
                status = "Mating & Birth",
                description = "Courtship pods assemble; pregnant mothers give birth in sheltered waters."
            ),
            WhaleSeason(
                month = "August",
                status = "Peak Season",
                description = "Optimal conditions for swimming with curious newborn calves and protective mothers."
            ),
            WhaleSeason(
                month = "September",
                status = "Peak Season",
                description = "High whale activity; underwater male whale songs echo through the deep bays."
            ),
            WhaleSeason(
                month = "October",
                status = "Active Swimming",
                description = "Calves grow stronger and display breach and tail-slap behaviors."
            ),
            WhaleSeason(
                month = "November",
                status = "Departure",
                description = "Whales commence their southward migration back to southern polar oceans."
            )
        ),
        rules = listOf(
            "Maximum of 4 swimmers plus 1 certified licensed local guide in the water at any time.",
            "Maintain a respectful distance (minimum 5 meters from whales, 10 meters from mothers with calves).",
            "No touching, chasing, or using underwater flash photography/scuba gear.",
            "Enter the water quietly without splashing to ensure calm interaction."
        ),
        facts = listOf(
            "Tonga declared its entire EEZ ocean area a Sanctuary for Whales in 1979.",
            "Humpback whales travel over 6,000 km from Antarctica to breed in Tonga.",
            "Calves are born weighing around 1 to 1.5 tons and gain up to 45kg of weight per day from milk!"
        )
    )

    val phrases: List<Phrase> = listOf(
        Phrase(
            tongan = "Mālō e lelei",
            english = "Hello / Good day",
            usage = "Universal greeting",
            pronunciation = "mah-LOH eh leh-LEH-ee"
        ),
        Phrase(
            tongan = "Mālō 'aupito",
            english = "Thank you very much",
            usage = "Polite gratitude",
            pronunciation = "mah-LOH ow-PEE-toh"
        ),
        Phrase(
            tongan = "'Ofa atu",
            english = "With love / Warm regards",
            usage = "Farewell or sign-off",
            pronunciation = "oh-FAH ah-TOO"
        ),
        Phrase(
            tongan = "Fēfe hake?",
            english = "How are you?",
            usage = "Casual check-in",
            pronunciation = "FEH-feh HAH-keh"
        ),
        Phrase(
            tongan = "Sai pē",
            english = "I am fine / All good",
            usage = "Response to Fēfe hake",
            pronunciation = "SAY peh"
        ),
        Phrase(
            tongan = "Tāufaka'ofa",
            english = "Beautiful / Magnificent",
            usage = "Praising sights & music",
            pronunciation = "tow-fah-kah-OH-fah"
        ),
        Phrase(
            tongan = "Nofo ā",
            english = "Goodbye (to one staying)",
            usage = "Leaving someone",
            pronunciation = "NOH-foh ah"
        ),
        Phrase(
            tongan = "'Alu ā",
            english = "Goodbye (to one leaving)",
            usage = "Said by host to guest",
            pronunciation = "AH-loo ah"
        )
    )

    val budgetTiers: List<BudgetTier> = listOf(
        BudgetTier(
            tierKey = "backpacker",
            label = "Budget Backpacker",
            costTOP = 130.0,
            desc = "Guesthouses, local food stalls, public buses & shared ferry routes."
        ),
        BudgetTier(
            tierKey = "midrange",
            label = "Island Explorer",
            costTOP = 320.0,
            desc = "Beachfront bungalows, local tours, dining out & inter-island flights."
        ),
        BudgetTier(
            tierKey = "luxury",
            label = "Royal Sanctuary",
            costTOP = 750.0,
            desc = "Private island resorts, private yacht charters & guided whale swims."
        )
    )

    val visaInfo: List<VisaInfo> = listOf(
        VisaInfo(
            region = "Passport Holders (US, AU, NZ, EU, UK, CA, JP)",
            rule = "Free 30-day Visitor Visa granted upon arrival.",
            requirements = "Valid passport (6+ months remaining), return ticket, proof of sufficient funds."
        ),
        VisaInfo(
            region = "Pacific Island Nations (PIF)",
            rule = "Free 30-day Visa on Arrival.",
            requirements = "Onward ticket & passport."
        ),
        VisaInfo(
            region = "Other Countries",
            rule = "Requires pre-arranged entry permit from Tongan Immigration before arrival.",
            requirements = "Submit online application 1 month prior."
        )
    )

    val emergencyContacts = listOf(
        Triple("Vaiola Central Hospital (Nuku'alofa)", "+676 23-200", "24/7 emergency medical care & trauma"),
        Triple("Tonga Police Emergency", "922", "National police emergency dispatch"),
        Triple("Maritime Search & Rescue", "911", "Emergency sea rescue & distress response")
    )

    fun getIslandById(id: String): Island? = islands.find { it.id == id }
}
