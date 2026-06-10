package com.consica.code

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.consica.code.data.local.AppDatabase

val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "consica_prefs")

class ConsicaCodeApp : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getInstance(this)
    }

    companion object {
        lateinit var instance: ConsicaCodeApp
            private set
    }
}
