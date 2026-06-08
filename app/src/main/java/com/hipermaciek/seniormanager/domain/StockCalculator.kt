package com.hipermaciek.seniormanager.domain

import kotlin.math.floor

object StockCalculator {
    fun remainingDays(currentStock: Int, dailyDose: Int): Int {
        if (dailyDose <= 0 || currentStock <= 0) return 0
        return floor(currentStock.toDouble() / dailyDose.toDouble()).toInt()
    }

    fun stockAlertLevel(remainingDays: Int): String = when {
        remainingDays <= 0 -> "CRITICAL_EMPTY"
        remainingDays <= 3 -> "CRITICAL_LOW"
        remainingDays <= 7 -> "LOW_STOCK"
        else -> "OK"
    }
}
