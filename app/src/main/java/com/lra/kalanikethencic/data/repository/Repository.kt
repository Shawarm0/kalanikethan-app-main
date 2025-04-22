package com.lra.kalanikethencic.data.repository

import com.lra.kalanikethencic.data.model.Class
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.model.StudentClass
import com.lra.kalanikethencic.data.remote.SupabaseClientProvider
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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

    // Get all classes as a Flow for real-time updates
    fun getAllClasses(): Flow<List<Class>> {
        return client.from("classes").selectAsFlow(Class::classId)
    }

    suspend fun getStudentsForClass(classId: Int): Flow<List<Student>> {
        val studentsFromClass = client
            .from("student_classes").select(columns = Columns.list("student_id", "class_id")) {
                filter {
                    Class::classId eq classId
                }
            } .decodeList<StudentClass>()

        val studentIds = studentsFromClass.map { it.studentId }

        // Step 2: Return a filtered flow from getAllStudents()
        return getAllStudents().map { studentList ->
            studentList.filter { it.studentId in studentIds }
        }
    }


}
