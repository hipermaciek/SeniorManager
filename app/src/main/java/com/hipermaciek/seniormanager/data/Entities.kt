package com.hipermaciek.seniormanager.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class SeniorProfile(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val bloodType: String,
    val diagnoses: String,
    val allergies: String
)

@Entity(indices = [Index("profileId")])
data class Medication(
    @PrimaryKey val id: String,
    val profileId: String,
    val name: String,
    val dose: String,
    val frequency: String,
    val timesPerDay: Int,
    val currentStock: Int,
    val totalQuantity: Int,
    val startDate: Long,
    val barcode: String?
)

@Entity(indices = [Index("profileId")])
data class HealthEntry(
    @PrimaryKey val id: String,
    val profileId: String,
    val timestamp: Long,
    val bloodPressure: String?,
    val heartRate: Int?,
    val glucose: Float?,
    val weight: Float?,
    val temperature: Float?,
    val painLevel: Int?,
    val wellbeing: String?,
    val fatigue: String?,
    val sleep: String?,
    val symptoms: String,
    val photoPath: String?,
    val voiceNotePath: String?,
    val eventType: String?
)

@Entity(indices = [Index("profileId")])
data class RefillEvent(
    @PrimaryKey val id: String,
    val profileId: String,
    val medicationId: String,
    val status: String,
    val reminderAt: Long
)

@Entity(indices = [Index("profileId")])
data class AlertEvent(
    @PrimaryKey val id: String,
    val profileId: String,
    val type: String,
    val priority: String,
    val message: String,
    val createdAt: Long
)

@Entity(indices = [Index("profileId")])
data class EmergencyEvent(
    @PrimaryKey val id: String,
    val profileId: String,
    val type: String,
    val triggeredAt: Long,
    val notes: String?
)
