package com.lra.kalanikethencic.data.repository

import android.util.Log
import com.lra.kalanikethencic.data.model.Class
import com.lra.kalanikethencic.data.model.Family
import com.lra.kalanikethencic.data.model.PaymentHistory
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
import kotlinx.coroutines.flow.first
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

    suspend fun getStudentsForClass(classId: Int): List<Student> {
        val studentsFromClass = client
            .from("student_classes").select(columns = Columns.list("student_id", "class_id")) {
                filter {
                    Class::classId eq classId
                }
            }.decodeList<StudentClass>()

        val studentIds = studentsFromClass.map { it.studentId }

        // Collect the flow to get the list
        val allStudents = getAllStudents().first()

        // Filter and return the list
        return allStudents.filter { it.studentId in studentIds }
    }

    fun getPaymentHistory(): Flow<List<PaymentHistory>> {
        return client
            .from("payment_history")
            .selectAsFlow(primaryKey = PaymentHistory::paymentId)
    }

    fun getFamilyNames(): Flow<List<Family>> {
        return client
            .from("families")
            .selectAsFlow(primaryKey = Family::familyId)
    }
}