package com.hipermaciek.seniormanager.ui

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hipermaciek.seniormanager.domain.EmergencyAction
import com.hipermaciek.seniormanager.system.EmergencyDispatcher
import com.hipermaciek.seniormanager.system.StockWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions.launch(arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS))
        scheduleBackgroundWork()

        setContent {
            MaterialTheme {
                MainScreen(
                    vm = vm,
                    onCaregiverCall = { EmergencyDispatcher.callCaregiver(this, "+48123456789") },
                    on112Call = { EmergencyDispatcher.call112(this) },
                    onWarning = { Toast.makeText(this, "Kliknij jeszcze ${it}x aby zadzwonić 112", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    private fun scheduleBackgroundWork() {
        val request = PeriodicWorkRequestBuilder<StockWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "stock-worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}

@Composable
private fun MainScreen(
    vm: MainViewModel,
    onCaregiverCall: () -> Unit,
    on112Call: () -> Unit,
    onWarning: (Int) -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var medName by remember { mutableStateOf("Metformina") }
    var medDose by remember { mutableStateOf("500mg") }
    var medStock by remember { mutableStateOf("30") }
    var medTimes by remember { mutableStateOf("2") }
    var bp by remember { mutableStateOf("120/80") }
    var pulse by remember { mutableStateOf("72") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Senior Health Manager", style = MaterialTheme.typography.headlineSmall)
        Text("Profile: Senior A / Senior B (offline-first)")

        OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text("Lek") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = medDose, onValueChange = { medDose = it }, label = { Text("Dawka") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = medTimes, onValueChange = { medTimes = it }, label = { Text("Razy/dzień") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = medStock, onValueChange = { medStock = it }, label = { Text("Zapas") }, modifier = Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                vm.addMedication("senior_a", medName, medDose, medTimes.toIntOrNull() ?: 1, medStock.toIntOrNull() ?: 0, null)
            }) { Text("Dodaj lek") }

            Button(onClick = onCaregiverCall) { Text("📞 Opiekun") }
        }

        Text("Dzienniczek zdrowia")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = bp, onValueChange = { bp = it }, label = { Text("Ciśnienie") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = pulse, onValueChange = { pulse = it }, label = { Text("Tętno") }, modifier = Modifier.weight(1f))
        }
        Button(onClick = { vm.addHealthEntry("senior_a", bp, pulse.toIntOrNull() ?: 0) }) { Text("Dodaj wpis") }

        Button(onClick = {
            when (val action = vm.emergencyClick()) {
                EmergencyAction.Call112 -> on112Call()
                is EmergencyAction.Warning -> onWarning(action.remainingClicks)
            }
        }) {
            Text("🚨 112 (3 kliknięcia <2s)")
        }

        state.lastAlert?.let { Text("Alert: ${it.message} [${it.priority}]") }
        state.lastDiaryMessage?.let { Text(it) }
    }
}
