package com.hipermaciek.seniormanager.widget

import android.content.Context
import android.content.Intent
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.text.Text
import com.hipermaciek.seniormanager.ui.MainActivity

class MedicationWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Column(modifier = GlanceModifier.fillMaxWidth()) {
                Text("Najbliższy lek")
                Text("Metformina 500mg")
                Text("08:00 - status: oczekuje")
                Row {
                    Button(text = "WZIĘTE", onClick = actionStartActivity<MainActivity>())
                    Button(text = "ODŁÓŻ", onClick = actionStartActivity<MainActivity>())
                }
                Row {
                    Button(text = "📞", onClick = actionStartActivity<MainActivity>())
                    Button(text = "🚨112", onClick = actionStartActivity<MainActivity>())
                }
            }
        }
    }
}

class MedicationWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MedicationWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }
}
