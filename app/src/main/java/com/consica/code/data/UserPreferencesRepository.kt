package com.consica.code.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val AGE_GROUP = stringPreferencesKey("age_group")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val PRO_MODE_UNLOCKED = booleanPreferencesKey("pro_mode_unlocked")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val ageGroup: Flow<String> = dataStore.data.map { preferences ->
        preferences[AGE_GROUP] ?: "8-12"
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    val isProModeUnlocked: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PRO_MODE_UNLOCKED] ?: false
    }

    suspend fun saveAgeGroup(ageGroup: String) {
        dataStore.edit { preferences ->
            preferences[AGE_GROUP] = ageGroup
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun unlockProMode(unlocked: Boolean) {
        dataStore.edit { preferences ->
            preferences[PRO_MODE_UNLOCKED] = unlocked
        }
    }
}
