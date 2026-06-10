package com.consica.code.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, ProgressEntity::class], version = 1, exportSchema = false)
abstract class BiomeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: BiomeDatabase? = null

        fun getDatabase(context: Context): BiomeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BiomeDatabase::class.java,
                    "biome_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
