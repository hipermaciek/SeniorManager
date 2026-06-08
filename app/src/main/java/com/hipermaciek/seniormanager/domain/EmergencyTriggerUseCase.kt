package com.hipermaciek.seniormanager.domain

class EmergencyTriggerUseCase(
    private val tripleWindowMs: Long = 2_000L
) {
    private var clickCounter = 0
    private var firstClickTimestamp = 0L

    fun registerClick(nowMs: Long = System.currentTimeMillis()): EmergencyAction {
        if (firstClickTimestamp == 0L || nowMs - firstClickTimestamp > tripleWindowMs) {
            clickCounter = 0
            firstClickTimestamp = nowMs
        }

        clickCounter++

        return if (clickCounter >= 3) {
            clickCounter = 0
            firstClickTimestamp = 0L
            EmergencyAction.CALL_112
        } else {
            EmergencyAction.WARNING
        }
    }
}

enum class EmergencyAction {
    WARNING,
    CALL_112
}
