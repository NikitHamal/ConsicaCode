package com.consica.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.consica.code.navigation.ConsicaNavGraph
import com.consica.code.ui.theme.ConsicaCodeTheme
import com.consica.code.ui.onboarding.OnboardingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ConsicaCodeRoot()
        }
    }
}

@Composable
fun ConsicaCodeRoot() {
    val onboardingViewModel: OnboardingViewModel = viewModel()
    val onboardingComplete by onboardingViewModel.onboardingComplete.collectAsState(initial = false)

    ConsicaCodeTheme(
        darkTheme = false,
        highContrast = false,
        reducedMotion = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConsicaNavGraph(
                onboardingComplete = onboardingComplete
            )
        }
    }
}
