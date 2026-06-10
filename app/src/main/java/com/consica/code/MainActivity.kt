package com.consica.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.consica.code.ui.screens.*
import com.consica.code.ui.theme.ConsicaCodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConsicaCodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConsicaCodeApp()
                }
            }
        }
    }
}

@Composable
fun ConsicaCodeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(
                onComplete = { ageGroup ->
                    navController.navigate("biome_map") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("biome_map") {
            BiomeMapScreen(
                onNavigateToLesson = { lessonId ->
                    navController.navigate("tutorial/$lessonId")
                }
            )
        }
        composable("tutorial/{lessonId}") { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            TerraTutorialScreen(
                lessonId = lessonId,
                onStartCoding = {
                    navController.navigate("playground/$lessonId")
                }
            )
        }
        composable("playground/{lessonId}") { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            GreenCodePlaygroundScreen(
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("ecosystem/$lessonId") {
                        popUpTo("biome_map") { inclusive = false }
                    }
                }
            )
        }
        composable("ecosystem/{lessonId}") {
            EcosystemScreen(
                onContinue = {
                    navController.navigate("biome_map") {
                        popUpTo("biome_map") { inclusive = true }
                    }
                }
            )
        }
        composable("rewards") {
            RewardsScreen()
        }
    }
}
