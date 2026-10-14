package online.codyuhi.tongantourism.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object DiscoverRoute

@Serializable
object IslandsRoute

@Serializable
object CultureRoute

@Serializable
object WhalesRoute

@Serializable
object PlannerRoute

@Serializable
object EssentialsRoute

enum class NavDestination(
    val route: Any,
    val label: String,
    val icon: String
) {
    Discover(DiscoverRoute, "Discover", "🌴"),
    Islands(IslandsRoute, "Islands", "🏝️"),
    Culture(CultureRoute, "Culture", "🌺"),
    Whales(WhalesRoute, "Whales", "🐋"),
    Planner(PlannerRoute, "Planner", "🗺️"),
    Essentials(EssentialsRoute, "Essentials", "🧭")
}
