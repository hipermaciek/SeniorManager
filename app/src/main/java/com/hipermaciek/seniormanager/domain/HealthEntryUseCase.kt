package com.hipermaciek.seniormanager.domain

import com.hipermaciek.seniormanager.data.HealthEntry
import com.hipermaciek.seniormanager.data.SeniorRepository

class HealthEntryUseCase(private val repository: SeniorRepository) {
    suspend operator fun invoke(entry: HealthEntry) {
        repository.upsertHealthEntry(entry)
    }
}
