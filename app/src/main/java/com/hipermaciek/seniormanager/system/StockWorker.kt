package com.hipermaciek.seniormanager.system

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hipermaciek.seniormanager.data.AppDatabase
import com.hipermaciek.seniormanager.data.SeniorRepository
import com.hipermaciek.seniormanager.domain.CalculateStockUseCase
import kotlinx.coroutines.flow.first

class StockWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val repository = SeniorRepository(AppDatabase.get(applicationContext).appDao())
        val profiles = repository.observeProfiles().first()
        val calculator = CalculateStockUseCase(repository)

        profiles.forEach { profile ->
            repository.observeMedications(profile.id).first().forEach { medication ->
                calculator(medication)?.let { alert ->
                    NotificationCenter.notify(
                        applicationContext,
                        alert.id.hashCode(),
                        "Senior Manager",
                        alert.message,
                        alert.priority
                    )
                }
            }
        }

        return Result.success()
    }
}
