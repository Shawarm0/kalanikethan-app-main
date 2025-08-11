package com.lra.kalanikethan.data.repository


import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.ui.screens.Add.PaymentData
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.request.UpsertRequestBuilder
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository {
    // Defines the client
    private val client = SupabaseClientProvider.client



    suspend fun getLastFamilyID(): Int {
        val count = client.from("families")
            .select {
                count(Count.EXACT)
            }
            .countOrNull()!!
        return count.toInt()
    }

    suspend fun updateStudent(student: Student) {
        try {
            client.from("students").upsert(student) {
                onConflict = "student_id"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun getAllStudents(): List<Student> {
        val students = client.from("students").select().decodeList<Student>()
        return students
    }

    suspend fun addFamily(family: Family): String? {
        client.from("families").insert(family)
        val familyID = client.from("families").select(){
            filter {
                FamilyWithID::familyName eq family.familyName
                FamilyWithID::email eq family.email
            }
        }.decodeSingle<FamilyWithID>()
        return familyID.familyID
    }

    suspend fun addStudent(student: Student) {
        client.from("students").insert(student)
    }

    suspend fun addParent(parent: Parent) {
        client.from("parents").insert(parent)
    }

    suspend fun addPaymentData(paymentPlan: PaymentPlan){
        client.from("payment_plan").insert(paymentPlan)
    }
}