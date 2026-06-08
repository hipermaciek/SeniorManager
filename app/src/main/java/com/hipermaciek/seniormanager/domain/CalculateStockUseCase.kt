package com.hipermaciek.seniormanager.domain

import com.hipermaciek.seniormanager.data.AlertEvent
import com.hipermaciek.seniormanager.data.Medication
import com.hipermaciek.seniormanager.data.SeniorRepository
import java.util.UUID

class CalculateStockUseCase(private val repository: SeniorRepository) {
    suspend operator fun invoke(medication: Medication): AlertEvent? {
        val dailyDose = medication.timesPerDay.coerceAtLeast(1)
        val remainingDays = StockCalculator.remainingDays(medication.currentStock, dailyDose)
        val level = StockCalculator.stockAlertLevel(remainingDays)
        if (level == "OK") return null

        val message = when (level) {
            "LOW_STOCK" -> "Za tydzień skończy się lek: ${medication.name}"
            "CRITICAL_LOW" -> "Krytycznie niski zapas leku: ${medication.name}"
            else -> "Brak leku: ${medication.name}"
        }

        return AlertEvent(
            id = UUID.randomUUID().toString(),
            profileId = medication.profileId,
            type = "stock",
            priority = if (level == "LOW_STOCK") "HIGH" else "CRITICAL",
            message = message,
            createdAt = System.currentTimeMillis()
        ).also { repository.addAlert(it) }
    }
}
