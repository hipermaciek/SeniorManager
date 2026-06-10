package com.hipermaciek.seniormanager.domain

import com.hipermaciek.seniormanager.data.Medication
import com.hipermaciek.seniormanager.data.SeniorRepository

class ManageMedicationUseCase(private val repository: SeniorRepository) {
    suspend operator fun invoke(medication: Medication) {
        repository.upsertMedication(medication)
    }
}
