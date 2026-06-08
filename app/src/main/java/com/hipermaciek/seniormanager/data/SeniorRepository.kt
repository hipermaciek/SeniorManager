package com.hipermaciek.seniormanager.data

import kotlinx.coroutines.flow.Flow

class SeniorRepository(private val dao: AppDao) {
    fun observeProfiles(): Flow<List<SeniorProfile>> = dao.observeProfiles()
    fun observeMedications(profileId: String): Flow<List<Medication>> = dao.observeMedications(profileId)

    suspend fun upsertProfile(profile: SeniorProfile) = dao.upsertProfile(profile)
    suspend fun upsertMedication(medication: Medication) = dao.upsertMedication(medication)
    suspend fun upsertHealthEntry(entry: HealthEntry) = dao.upsertHealthEntry(entry)
    suspend fun getNearestMedication(): Medication? = dao.getNearestMedication()
    suspend fun getRecentHealthEntries(profileId: String, limit: Int): List<HealthEntry> =
        dao.getRecentHealthEntries(profileId, limit)

    suspend fun addAlert(alert: AlertEvent) = dao.addAlert(alert)
    suspend fun addEmergency(event: EmergencyEvent) = dao.addEmergency(event)
}
