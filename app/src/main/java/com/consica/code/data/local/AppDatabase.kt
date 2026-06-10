package com.consica.code.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.consica.code.data.local.dao.*
import com.consica.code.data.local.entity.*

@Database(
    entities = [
        UserProgressEntity::class,
        CompletedLessonEntity::class,
        WorkspaceEntity::class,
        BadgeEntity::class,
        CodeHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
    abstract fun lessonDao(): LessonDao
    abstract fun workspaceDao(): WorkspaceDao
    abstract fun badgeDao(): BadgeDao
    abstract fun codeHistoryDao(): CodeHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "consica_code_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
