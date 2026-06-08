package com.hipermaciek.seniormanager.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class EmergencyTriggerUseCaseTest {
    @Test
    fun `calls 112 on 3rd click in time window`() {
        val useCase = EmergencyTriggerUseCase(tripleWindowMs = 2_000)

        assertEquals(EmergencyAction.WARNING, useCase.registerClick(1_000))
        assertEquals(EmergencyAction.WARNING, useCase.registerClick(1_500))
        assertEquals(EmergencyAction.CALL_112, useCase.registerClick(2_000))
    }

    @Test
    fun `resets counter after timeout`() {
        val useCase = EmergencyTriggerUseCase(tripleWindowMs = 2_000)

        assertEquals(EmergencyAction.WARNING, useCase.registerClick(1_000))
        assertEquals(EmergencyAction.WARNING, useCase.registerClick(4_000))
    }
}
