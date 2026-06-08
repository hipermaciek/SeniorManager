package com.hipermaciek.seniormanager.system

import android.content.Context
import android.content.Intent
import android.net.Uri

object EmergencyDispatcher {
    fun callCaregiver(context: Context, phoneNumber: String): Intent {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return intent
    }

    fun call112(context: Context): Intent {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:112"))
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return intent
    }
}
