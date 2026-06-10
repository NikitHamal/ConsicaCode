package com.consica.code.ui.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.consica.code.ConsicaCodeApp
import com.consica.code.data.model.UserProfile
import com.consica.code.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LessonViewModel : ViewModel() {
    private val app = ConsicaCodeApp.instance
    private val repository = AppRepository(app.database, app.dataStore)

    val userProfile: Flow<UserProfile> = repository.userProfile

    private val _showContent = MutableStateFlow(false)
    val showContent: StateFlow<Boolean> = _showContent.asStateFlow()

    fun startReveal() {
        viewModelScope.launch {
            delay(400) // Typewriter-style reveal delay
            _showContent.value = true
        }
    }
}
