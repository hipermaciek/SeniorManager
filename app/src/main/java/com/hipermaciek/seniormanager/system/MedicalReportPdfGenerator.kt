package com.hipermaciek.seniormanager.system

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.hipermaciek.seniormanager.data.HealthEntry
import com.hipermaciek.seniormanager.data.Medication
import com.hipermaciek.seniormanager.data.SeniorProfile
import java.io.File

object MedicalReportPdfGenerator {
    fun generate(
        context: Context,
        profile: SeniorProfile,
        medications: List<Medication>,
        entries: List<HealthEntry>
    ): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { textSize = 12f }

        var y = 40f
        fun line(text: String) {
            canvas.drawText(text, 40f, y, paint)
            y += 20f
        }

        line("Emergency summary")
        line("Pacjent: ${profile.name}, wiek ${profile.age}, grupa krwi ${profile.bloodType}")
        line("Schorzenia: ${profile.diagnoses}")
        line("Alergie: ${profile.allergies}")
        line("Leki:")
        medications.forEach { line("- ${it.name} ${it.dose}, ${it.frequency}, zapas: ${it.currentStock}") }
        line("Pomiary (ostatnie ${entries.size}):")
        entries.take(10).forEach { line("- ${it.timestamp}: BP ${it.bloodPressure ?: "-"}, HR ${it.heartRate ?: "-"}") }

        document.finishPage(page)

        val outFile = File(context.filesDir, "medical_report_${profile.id}.pdf")
        outFile.outputStream().use(document::writeTo)
        document.close()
        return outFile
    }
}
