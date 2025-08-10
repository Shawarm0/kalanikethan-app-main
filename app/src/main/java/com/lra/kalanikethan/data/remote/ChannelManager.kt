package com.lra.kalanikethan.data.remote

import android.util.Log
import com.lra.kalanikethan.data.models.Channel
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow


object ChannelManager {
    // single scope to host shared flows
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val studentsChannel = Channel(client.channel("students-listener"), "students", managerScope)
    private val historyChannel = Channel(client.channel("history-listener"), "history", managerScope)

    suspend fun subscribeToStudentsChannel(): Flow<PostgresAction> {
        Log.i("ChannelManager", "Attempting to connect to students channel")
        studentsChannel.subscribe()
        return studentsChannel.getFlow()
    }
    suspend fun subscribeToHistoryChannel(): Channel {
        unsubscribeFromAllChannels()
        Log.i("Database-ChannelManager", "Attempting to connect to history channel")
        historyChannel.subscribe()
        return historyChannel
    }

    suspend fun unsubscribeFromAllChannels() {
        studentsChannel.unsubscribe()
        historyChannel.unsubscribe()
        Log.i("Database-ChannelManager", "Unsubscribed from all channels")
    }
}


