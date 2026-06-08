package com.hipermaciek.seniormanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: SeniorProfile)

    @Query("SELECT * FROM SeniorProfile ORDER BY name LIMIT 2")
    fun observeProfiles(): Flow<List<SeniorProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMedication(medication: Medication)

    @Query("SELECT * FROM Medication WHERE profileId = :profileId")
    fun observeMedications(profileId: String): Flow<List<Medication>>

    @Query("SELECT * FROM Medication ORDER BY startDate DESC LIMIT 1")
    suspend fun getMostRecentMedication(): Medication?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHealthEntry(entry: HealthEntry)

    @Query("SELECT * FROM HealthEntry WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentHealthEntries(profileId: String, limit: Int): List<HealthEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlert(alert: AlertEvent)

    @Query("SELECT * FROM AlertEvent WHERE profileId = :profileId ORDER BY createdAt DESC")
    suspend fun getAlerts(profileId: String): List<AlertEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmergency(event: EmergencyEvent)
}
