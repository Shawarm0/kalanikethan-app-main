package com.lra.kalanikethan.util

import android.os.Environment
import android.util.Log
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Student
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

object PdfExporter {

    fun exportHistory(
        histories: List<History>,
        students: List<Student>,
        classes: List<Class>,
        employees: List<Employee>
    ) {
        try {
            val grouped = groupHistoriesByDay(histories)
            val sb = StringBuilder()
            sb.appendLine("Kalanikethan Attendance Report")
            sb.appendLine("Generated: ${LocalDate.now()}")
            sb.appendLine("=".repeat(60))

            for (dayGroup in grouped) {
                if (dayGroup.isEmpty()) continue
                sb.appendLine("\nDate: ${dayGroup.first().date}")
                sb.appendLine("-".repeat(40))
                for (h in dayGroup) {
                    val student = students.find { it.studentId == h.studentID }
                    val name = student?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
                    val signIn = convertLongToTime(h.signInTime) ?: "N/A"
                    val signOut = convertLongToTime(h.signOutTime) ?: "N/A"
                    sb.appendLine("  $name | In: $signIn | Out: $signOut")
                }
            }

            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, "kalanikethan_report_${System.currentTimeMillis()}.txt")
            FileOutputStream(file).use { it.write(sb.toString().toByteArray()) }
            Log.d("PdfExporter", "Report saved to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("PdfExporter", "Failed to export", e)
        }
    }
}
