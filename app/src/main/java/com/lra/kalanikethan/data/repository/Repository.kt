package com.lra.kalanikethan.data.repository

import android.util.Log
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.postgresListDataFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

class Repository {
    // Defines the client
    private val client = SupabaseClientProvider.client



    /**
     * Initializes the variables and sets up the channel called students-listener
     * */
    suspend fun initializeStudentChannel(scope: CoroutineScope): Flow<PostgresAction> {
        val channel = client.channel("students-listener")
        val dataFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "students"
        }
        channel.subscribe()
        return dataFlow
    }

}