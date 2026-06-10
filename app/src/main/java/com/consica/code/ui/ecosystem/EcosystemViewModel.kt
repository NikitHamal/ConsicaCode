package com.consica.code.ui.ecosystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.consica.code.ConsicaCodeApp
import com.consica.code.data.model.UserProfile
import com.consica.code.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EcosystemViewModel : ViewModel() {
    private val app = ConsicaCodeApp.instance
    private val repository = AppRepository(app.database, app.dataStore)

    val userProfile: Flow<UserProfile> = repository.userProfile

    private val _isSuccess = MutableStateFlow(true)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _earnedXp = MutableStateFlow(0)
    val earnedXp: StateFlow<Int> = _earnedXp.asStateFlow()

    private val _showCelebration = MutableStateFlow(false)
    val showCelebration: StateFlow<Boolean> = _showCelebration.asStateFlow()

    fun processResult(lessonId: String) {
        viewModelScope.launch {
            // In practice, this would check lesson completion
            _isSuccess.value = true
            _earnedXp.value = 50

            delay(600)
            _showCelebration.value = true

            delay(2000)
            _showCelebration.value = false
        }
    }
}
