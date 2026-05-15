package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from

class StudentRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getAllStudents(): List<Student> {
        return client.from(Tables.STUDENTS).select().decodeList<Student>()
    }

    suspend fun addStudent(student: Student) {
        client.from(Tables.STUDENTS).insert(student)
    }

    suspend fun getStudentsByFamilyId(familyId: String): List<Student> {
        return client.from(Tables.STUDENTS).select {
            filter {
                Student::familyId eq familyId
            }
        }.decodeList<Student>()
    }

    suspend fun updateStudent(student: Student) {
        client.from(Tables.STUDENTS).upsert(student) {
            onConflict = "student_id"
        }
    }

    suspend fun deleteStudent(studentId: Int) {
        client.from(Tables.STUDENTS).delete {
            filter {
                Student::studentId eq studentId
            }
        }
    }
}
