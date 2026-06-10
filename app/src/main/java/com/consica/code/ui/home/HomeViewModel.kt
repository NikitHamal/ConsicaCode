package com.consica.code.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.consica.code.ConsicaCodeApp
import com.consica.code.data.local.entity.UserProgressEntity
import com.consica.code.data.model.UserProfile
import com.consica.code.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val app = ConsicaCodeApp.instance
    private val repository = AppRepository(app.database, app.dataStore)

    val userProfile: Flow<UserProfile> = repository.userProfile
    val progress: StateFlow<UserProgressEntity?> = repository.userProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isOffline = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.updateStreak()
            repository.checkProfessionalModeUnlock()
        }
    }

    fun setOfflineMode(offline: Boolean) {
        isOffline.value = offline
    }
}
