package com.hipermaciek.seniormanager.system

import android.content.Context
import android.telephony.SmsManager

object SmsGateway {
    fun sendCriticalAlert(context: Context, phoneNumber: String, message: String): Result<Unit> = runCatching {
        val manager = context.getSystemService(SmsManager::class.java)
        manager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}
