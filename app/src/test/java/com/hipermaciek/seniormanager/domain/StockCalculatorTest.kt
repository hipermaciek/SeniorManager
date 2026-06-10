package com.hipermaciek.seniormanager.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class StockCalculatorTest {
    @Test
    fun `returns warning level for weekly threshold`() {
        val remaining = StockCalculator.remainingDays(currentStock = 14, dailyDose = 2)
        assertEquals(7, remaining)
        assertEquals("LOW_STOCK", StockCalculator.stockAlertLevel(remaining))
    }

    @Test
    fun `returns critical for empty stock`() {
        val remaining = StockCalculator.remainingDays(currentStock = 0, dailyDose = 2)
        assertEquals(0, remaining)
        assertEquals("CRITICAL_EMPTY", StockCalculator.stockAlertLevel(remaining))
    }
}
