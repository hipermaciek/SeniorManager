package com.hipermaciek.seniormanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hipermaciek.seniormanager.data.AlertEvent
import com.hipermaciek.seniormanager.data.AppDatabase
import com.hipermaciek.seniormanager.data.HealthEntry
import com.hipermaciek.seniormanager.data.Medication
import com.hipermaciek.seniormanager.data.SeniorProfile
import com.hipermaciek.seniormanager.data.SeniorRepository
import com.hipermaciek.seniormanager.domain.CalculateStockUseCase
import com.hipermaciek.seniormanager.domain.EmergencyAction
import com.hipermaciek.seniormanager.domain.EmergencyTriggerUseCase
import com.hipermaciek.seniormanager.domain.HealthEntryUseCase
import com.hipermaciek.seniormanager.domain.ManageMedicationUseCase
import com.hipermaciek.seniormanager.system.MedicalReportPdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SeniorRepository(AppDatabase.get(application).appDao())
    private val medicationUseCase = ManageMedicationUseCase(repository)
    private val stockUseCase = CalculateStockUseCase(repository)
    private val healthEntryUseCase = HealthEntryUseCase(repository)
    private val emergencyUseCase = EmergencyTriggerUseCase()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        seedProfiles()
    }

    private fun seedProfiles() {
        viewModelScope.launch {
            repository.upsertProfile(SeniorProfile("senior_a", "Senior A", 72, "A+", "nadciśnienie", "penicylina:high"))
            repository.upsertProfile(SeniorProfile("senior_b", "Senior B", 75, "0+", "cukrzyca", "brak"))
        }
    }

    fun addMedication(profileId: String, name: String, dose: String, timesPerDay: Int, stock: Int, barcode: String?) {
        viewModelScope.launch {
            val medication = Medication(
                id = UUID.randomUUID().toString(),
                profileId = profileId,
                name = name,
                dose = dose,
                frequency = "daily",
                timesPerDay = timesPerDay,
                currentStock = stock,
                totalQuantity = stock,
                startDate = System.currentTimeMillis().toString(),
                barcode = barcode
            )
            medicationUseCase(medication)
            val alert = stockUseCase(medication)
            _uiState.value = _uiState.value.copy(lastAlert = alert)
        }
    }

    fun addHealthEntry(profileId: String, bp: String, pulse: Int) {
        viewModelScope.launch {
            healthEntryUseCase(
                HealthEntry(
                    id = UUID.randomUUID().toString(),
                    profileId = profileId,
                    timestamp = System.currentTimeMillis(),
                    bloodPressure = bp,
                    heartRate = pulse,
                    glucose = null,
                    weight = null,
                    temperature = null,
                    painLevel = null,
                    wellbeing = null,
                    fatigue = null,
                    sleep = null,
                    symptoms = "",
                    photoPath = null,
                    voiceNotePath = null,
                    eventType = null
                )
            )
            _uiState.value = _uiState.value.copy(lastDiaryMessage = "Dodano wpis zdrowia")
        }
    }

    fun emergencyClick(): EmergencyAction {
        val action = emergencyUseCase.registerClick()
        _uiState.value = _uiState.value.copy(lastEmergencyAction = action)
        return action
    }

    fun generatePdf(profile: SeniorProfile, medications: List<Medication>, entries: List<HealthEntry>) {
        viewModelScope.launch {
            val file = MedicalReportPdfGenerator.generate(getApplication(), profile, medications, entries)
            _uiState.value = _uiState.value.copy(lastDiaryMessage = "PDF: ${file.name}")
        }
    }
}

data class MainUiState(
    val lastAlert: AlertEvent? = null,
    val lastEmergencyAction: EmergencyAction? = null,
    val lastDiaryMessage: String? = null
)
