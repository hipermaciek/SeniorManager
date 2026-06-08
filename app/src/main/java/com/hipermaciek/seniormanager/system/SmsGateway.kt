package com.hipermaciek.seniormanager.system

import android.telephony.SmsManager

object SmsGateway {
    fun sendCriticalAlert(phoneNumber: String, message: String): Result<Unit> = runCatching {
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null)
    }
}
