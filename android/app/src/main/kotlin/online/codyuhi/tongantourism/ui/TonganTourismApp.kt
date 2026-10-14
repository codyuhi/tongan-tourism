package online.codyuhi.tongantourism.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import online.codyuhi.tongantourism.di.AppContainer
import online.codyuhi.tongantourism.ui.components.CedarTopAppBar
import online.codyuhi.tongantourism.ui.navigation.CultureRoute
import online.codyuhi.tongantourism.ui.navigation.DiscoverRoute
import online.codyuhi.tongantourism.ui.navigation.EssentialsRoute
import online.codyuhi.tongantourism.ui.navigation.IslandsRoute
import online.codyuhi.tongantourism.ui.navigation.NavDestination
import online.codyuhi.tongantourism.ui.navigation.PlannerRoute
import online.codyuhi.tongantourism.ui.navigation.WhalesRoute
import online.codyuhi.tongantourism.ui.screens.culture.CultureScreen
import online.codyuhi.tongantourism.ui.screens.culture.CultureViewModel
import online.codyuhi.tongantourism.ui.screens.discover.DiscoverScreen
import online.codyuhi.tongantourism.ui.screens.discover.DiscoverViewModel
import online.codyuhi.tongantourism.ui.screens.essentials.EssentialsScreen
import online.codyuhi.tongantourism.ui.screens.essentials.EssentialsViewModel
import online.codyuhi.tongantourism.ui.screens.islands.IslandsScreen
import online.codyuhi.tongantourism.ui.screens.islands.IslandsViewModel
import online.codyuhi.tongantourism.ui.screens.planner.PlannerScreen
import online.codyuhi.tongantourism.ui.screens.planner.PlannerViewModel
import online.codyuhi.tongantourism.ui.screens.whales.WhalesScreen
import online.codyuhi.tongantourism.ui.screens.whales.WhalesViewModel
import online.codyuhi.tongantourism.ui.theme.Cedar
import online.codyuhi.tongantourism.ui.theme.ThemeMode

@Composable
fun TonganTourismApp(
    container: AppContainer,
    currentThemeMode: ThemeMode,
    onToggleTheme: () -> Unit,
    initialDestination: NavDestination = NavDestination.Discover
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val discoverViewModel = remember {
        DiscoverViewModel(
            container.tongaDataRepository,
            container.weatherRepository,
            container.currencyRepository
        )
    }
    val islandsViewModel = remember { IslandsViewModel(container.tongaDataRepository) }
    val cultureViewModel = remember { CultureViewModel(container.tongaDataRepository) }
    val whalesViewModel = remember { WhalesViewModel(container.tongaDataRepository, container.soundEngine) }
    val plannerViewModel = remember { PlannerViewModel(container.itineraryRepository) }
    val essentialsViewModel = remember {
        EssentialsViewModel(container.tongaDataRepository, container.currencyRepository)
    }

    val plannerUiState by plannerViewModel.uiState.collectAsState()
    val savedTripsCount = plannerUiState.savedItineraries.size

    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val isWide = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val navSuiteType = if (isWide) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteType.NavigationBar
    }

    NavigationSuiteScaffold(
        layoutType = navSuiteType,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Cedar.colors.surface,
            navigationRailContainerColor = Cedar.colors.surface
        ),
        navigationSuiteItems = {
            NavDestination.entries.forEach { dest ->
                val selected = currentRoute?.contains(dest.name, ignoreCase = true) == true
                item(
                    icon = { Text(text = dest.icon, fontSize = 20.sp) },
                    label = { Text(dest.label) },
                    selected = selected,
                    onClick = {
                        navController.navigate(dest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CedarTopAppBar(
                themeMode = currentThemeMode,
                onToggleTheme = onToggleTheme,
                savedTripsCount = savedTripsCount,
                onSavedTripsClick = {
                    navController.navigate(PlannerRoute) {
                        launchSingleTop = true
                    }
                }
            )

            Box(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = navController,
                    startDestination = initialDestination.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable<DiscoverRoute> {
                        DiscoverScreen(
                            viewModel = discoverViewModel,
                            onNavigateToIslands = {
                                navController.navigate(IslandsRoute) { launchSingleTop = true }
                            },
                            onNavigateToWhales = {
                                navController.navigate(WhalesRoute) { launchSingleTop = true }
                            },
                            onNavigateToPlanner = {
                                navController.navigate(PlannerRoute) { launchSingleTop = true }
                            },
                            onSelectIsland = { islandId ->
                                islandsViewModel.openIslandById(islandId)
                                navController.navigate(IslandsRoute) { launchSingleTop = true }
                            }
                        )
                    }

                    composable<IslandsRoute> {
                        IslandsScreen(
                            viewModel = islandsViewModel,
                            onPlanTripToIsland = { islandId ->
                                plannerViewModel.toggleIsland(islandId)
                                navController.navigate(PlannerRoute) { launchSingleTop = true }
                            }
                        )
                    }

                    composable<CultureRoute> {
                        CultureScreen(viewModel = cultureViewModel)
                    }

                    composable<WhalesRoute> {
                        WhalesScreen(viewModel = whalesViewModel)
                    }

                    composable<PlannerRoute> {
                        PlannerScreen(viewModel = plannerViewModel)
                    }

                    composable<EssentialsRoute> {
                        EssentialsScreen(viewModel = essentialsViewModel)
                    }
                }
            }
        }
    }
}
