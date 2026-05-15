package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.StudentClass
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from

class ClassRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getAllClasses(): List<Class> {
        return client.from(Tables.CLASSES).select().decodeList<Class>()
    }

    suspend fun updateClass(updatedClass: Class) {
        println(updatedClass)
        client.from(Tables.CLASSES).upsert(updatedClass) {
            onConflict = "class_id"
        }
    }

    suspend fun getStudentIdsForClass(classID: Int): List<Int> {
        return client.from(Tables.STUDENT_CLASSES).select() {
            filter {
                StudentClass::classId eq classID
            }
        }.decodeList<StudentClass>().map { it.studentId }
    }

    suspend fun addStudentToClass(studentId: Int, classId: Int) {
        client.from(Tables.STUDENT_CLASSES).insert(
            mapOf(
                "student_id" to studentId,
                "class_id" to classId
            )
        )
    }

    suspend fun removeStudentFromClass(studentId: Int, classId: Int) {
        client.from(Tables.STUDENT_CLASSES).delete {
            filter {
                StudentClass::classId eq classId
                StudentClass::studentId eq studentId
            }
        }
    }
}
