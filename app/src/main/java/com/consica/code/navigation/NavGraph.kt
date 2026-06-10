package com.consica.code.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.*
import com.consica.code.ui.home.BiomeMapScreen
import com.consica.code.ui.onboarding.OnboardingScreen
import com.consica.code.ui.playground.PlaygroundScreen
import com.consica.code.ui.puzzle.PuzzleScreen
import com.consica.code.ui.rewards.RewardsScreen
import com.consica.code.ui.settings.SettingsScreen
import com.consica.code.ui.path.LearningPathScreen
import com.consica.code.ui.workspace.WorkspaceScreen
import com.consica.code.ui.lesson.TerrasNestScreen
import com.consica.code.ui.ecosystem.EcosystemScreen

sealed class ConsicaRoute(val route: String) {
    object Onboarding : ConsicaRoute("onboarding")
    object Home : ConsicaRoute("home")
    object Playground : ConsicaRoute("playground/{lessonId}") {
        fun createRoute(lessonId: String = "") = "playground/$lessonId"
    }
    object Puzzles : ConsicaRoute("puzzles")
    object Rewards : ConsicaRoute("rewards")
    object Path : ConsicaRoute("path")
    object Settings : ConsicaRoute("settings")
    object Workspace : ConsicaRoute("workspace")
    object Lesson : ConsicaRoute("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
    object Ecosystem : ConsicaRoute("ecosystem/{lessonId}") {
        fun createRoute(lessonId: String) = "ecosystem/$lessonId"
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(ConsicaRoute.Home.route, "Home", Icons.Filled.Nature, Icons.Outlined.Nature),
    BottomNavItem(ConsicaRoute.Playground.createRoute(), "Playground", Icons.Filled.Code, Icons.Outlined.Code),
    BottomNavItem(ConsicaRoute.Puzzles.route, "Puzzles", Icons.Filled.Extension, Icons.Outlined.Extension),
    BottomNavItem(ConsicaRoute.Rewards.route, "Rewards", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    BottomNavItem(ConsicaRoute.Settings.route, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsicaNavGraph(onboardingComplete: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (onboardingComplete) ConsicaRoute.Home.route else ConsicaRoute.Onboarding.route

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        ConsicaRoute.Home.route,
        ConsicaRoute.Playground.createRoute(),
        ConsicaRoute.Puzzles.route,
        ConsicaRoute.Rewards.route,
        ConsicaRoute.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = when {
                            currentRoute == ConsicaRoute.Home.route && item.route == ConsicaRoute.Home.route -> true
                            currentRoute?.startsWith("playground") == true && item.route.startsWith("playground") -> true
                            else -> currentRoute == item.route
                        }
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (item.route != currentRoute) {
                                    navController.navigate(item.route) {
                                        popUpTo(ConsicaRoute.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.outline,
                                unselectedTextColor = MaterialTheme.colorScheme.outline,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(initialAlpha = 0.3f) + slideInHorizontally(initialOffsetX = { it / 4 }) },
            exitTransition = { fadeOut(targetAlpha = 0.3f) + slideOutHorizontally(targetOffsetX = { -it / 4 }) }
        ) {
            composable(ConsicaRoute.Onboarding.route) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(ConsicaRoute.Home.route) {
                            popUpTo(ConsicaRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(ConsicaRoute.Home.route) {
                BiomeMapScreen(
                    onLessonClick = { lessonId ->
                        navController.navigate(ConsicaRoute.Lesson.createRoute(lessonId))
                    },
                    onPlaygroundClick = {
                        navController.navigate(ConsicaRoute.Playground.createRoute())
                    },
                    onPathClick = {
                        navController.navigate(ConsicaRoute.Path.route)
                    },
                    onWorkspaceClick = {
                        navController.navigate(ConsicaRoute.Workspace.route)
                    }
                )
            }
            composable(
                ConsicaRoute.Playground.route,
                arguments = listOf(navArgument("lessonId") { defaultValue = ""; type = NavType.StringType })
            ) { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                PlaygroundScreen(
                    lessonId = lessonId,
                    onRunComplete = { id ->
                        if (id.isNotBlank()) {
                            navController.navigate(ConsicaRoute.Ecosystem.createRoute(id))
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(ConsicaRoute.Puzzles.route) {
                PuzzleScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(ConsicaRoute.Rewards.route) {
                RewardsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(ConsicaRoute.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(ConsicaRoute.Path.route) {
                LearningPathScreen(
                    onLessonClick = { lessonId ->
                        navController.navigate(ConsicaRoute.Lesson.createRoute(lessonId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(ConsicaRoute.Workspace.route) {
                WorkspaceScreen(
                    onOpenPlayground = { workspaceId ->
                        navController.navigate(ConsicaRoute.Playground.createRoute(workspaceId.toString()))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                ConsicaRoute.Lesson.route,
                arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
            ) { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "seed_001"
                TerrasNestScreen(
                    lessonId = lessonId,
                    onStartCoding = {
                        navController.navigate(ConsicaRoute.Playground.createRoute(lessonId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                ConsicaRoute.Ecosystem.route,
                arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
            ) { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                EcosystemScreen(
                    lessonId = lessonId,
                    onContinue = {
                        navController.navigate(ConsicaRoute.Home.route) {
                            popUpTo(ConsicaRoute.Home.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
