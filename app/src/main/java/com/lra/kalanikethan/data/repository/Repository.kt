package com.lra.kalanikethan.data.repository


import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

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



    suspend fun getAllStudents(): List<Student> {
        val students = client.from("students").select().decodeList<Student>()
        return students
    }

    /**
     * Initializes the variables and sets up the channel called students-listener
     * */
    suspend fun initializeStudentChannel(scope: CoroutineScope, channel: RealtimeChannel, dataFlow: Flow<PostgresAction>): Flow<PostgresAction> {
        channel.subscribe()
        return dataFlow
    }

    suspend fun unsubscribeFromChannel(channel: RealtimeChannel) {
        println("Unsubscribed")
        channel.unsubscribe()
    }

    suspend fun addFamily(family: Family) {
        client.from("families").insert(family)
    }

    suspend fun addStudent(student: Student) {
        client.from("students").insert(student)
    }

    suspend fun addParent(parent: Parent) {
        client.from("parents").insert(parent)
    }
}