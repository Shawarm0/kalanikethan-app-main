package com.lra.kalanikethan.data.repository

import android.util.Log
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from

class AttendanceRepository {
    private val client = SupabaseClientProvider.client

    suspend fun signInStudent(student: Student, history: History) {
        Log.i("Repository-SignIn", "Student: $student")
        try {
            client.from(Tables.STUDENTS).upsert(student) {
                onConflict = "student_id"
            }
            client.from(Tables.HISTORY).insert(history)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun signOutStudent(student: Student, history: History?, uid: String) {
        Log.i("Repository-SignOut", "Student: $student")
        try {
            client.from(Tables.STUDENTS).upsert(student) {
                onConflict = "student_id"
            }
            if (history != null) {
                val updatedHistory = history.copy(signOutTime = System.currentTimeMillis(), uid = uid)
                client.from(Tables.HISTORY).upsert(updatedHistory) {
                    onConflict = "history_id"
                }
            } else {
                Log.i("Repository-SignOut", "Student history not found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
