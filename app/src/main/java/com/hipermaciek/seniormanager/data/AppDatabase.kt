package com.hipermaciek.seniormanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SeniorProfile::class,
        Medication::class,
        HealthEntry::class,
        RefillEvent::class,
        AlertEvent::class,
        EmergencyEvent::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "senior_manager.db"
                ).build().also { instance = it }
            }
    }
}
