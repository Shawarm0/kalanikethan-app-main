package com.lra.kalanikethencic.data.repository

import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// All of this should be Supabase
class Repository @Inject constructor() {

    private val client = SupabaseClientProvider.client

    suspend fun getAllStudents(): List<Student> = withContext(Dispatchers.IO) {
        try {
            client.from("students")
                .select()
                .decodeList<Student>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return empty list if there's an error
        }
    }

    suspend fun updateStudent(student: Student): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("students")
                .upsert(student)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}
