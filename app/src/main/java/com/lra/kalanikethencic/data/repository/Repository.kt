package com.lra.kalanikethencic.data.repository

import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.remote.SupabaseClientProvider
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

// All of this should be Supabase
@OptIn(SupabaseExperimental::class)
class Repository @Inject constructor() {

    private val client = SupabaseClientProvider.client




    fun getAllStudents(): Flow<List<Student>> {
        return client.from("students").selectAsFlow(Student::studentId)
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
