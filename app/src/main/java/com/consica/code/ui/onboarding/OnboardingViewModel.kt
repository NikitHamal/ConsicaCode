package com.consica.code.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.consica.code.ConsicaCodeApp
import com.consica.code.data.model.*
import com.consica.code.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    private val app = ConsicaCodeApp.instance
    private val repository = AppRepository(app.database, app.dataStore)

    val onboardingComplete: Flow<Boolean> = repository.onboardingComplete

    private val _selectedAgeGroup = MutableStateFlow(AgeGroup.YOUNG)
    val selectedAgeGroup: StateFlow<AgeGroup> = _selectedAgeGroup.asStateFlow()

    private val _selectedGoal = MutableStateFlow(LearningGoal.EXPLORE)
    val selectedGoal: StateFlow<LearningGoal> = _selectedGoal.asStateFlow()

    private val _selectedExperience = MutableStateFlow(ExperienceLevel.BEGINNER)
    val selectedExperience: StateFlow<ExperienceLevel> = _selectedExperience.asStateFlow()

    private val _selectedTheme = MutableStateFlow(ThemeIntensity.PLAYFUL)
    val selectedTheme: StateFlow<ThemeIntensity> = _selectedTheme.asStateFlow()

    private val _selectedInterests = MutableStateFlow(setOf<String>())
    val selectedInterests: StateFlow<Set<String>> = _selectedInterests.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _ageValue = MutableStateFlow(10)
    val ageValue: StateFlow<Int> = _ageValue.asStateFlow()

    fun setAgeGroup(group: AgeGroup) {
        _selectedAgeGroup.value = group
        _ageValue.value = when (group) {
            AgeGroup.YOUNG -> 10
            AgeGroup.TEEN -> 14
            AgeGroup.YOUNG_ADULT -> 17
        }
    }

    fun setAge(age: Int) { _ageValue.value = age; _selectedAgeGroup.value = AgeGroup.fromAge(age) }
    fun setGoal(goal: LearningGoal) { _selectedGoal.value = goal }
    fun setExperience(level: ExperienceLevel) { _selectedExperience.value = level }
    fun setTheme(theme: ThemeIntensity) { _selectedTheme.value = theme }

    fun toggleInterest(interest: String) {
        _selectedInterests.update { current ->
            if (interest in current) current - interest else current + interest
        }
    }

    fun nextStep() { _currentStep.update { minOf(it + 1, 5) } }
    fun previousStep() { _currentStep.update { maxOf(it - 1, 0) } }

    fun completeOnboarding() {
        viewModelScope.launch {
            val profile = UserProfile(
                ageGroup = _selectedAgeGroup.value,
                age = _ageValue.value,
                learningGoal = _selectedGoal.value,
                experienceLevel = _selectedExperience.value,
                themeIntensity = _selectedTheme.value,
                interests = _selectedInterests.value,
                onboardingComplete = true
            )
            repository.completeOnboarding(profile)
        }
    }
}
