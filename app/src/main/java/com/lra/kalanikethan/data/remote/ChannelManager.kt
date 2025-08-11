package com.lra.kalanikethan.data.remote

import android.util.Log
import com.lra.kalanikethan.data.models.Channel
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * Manages Supabase realtime channels for different data streams.
 *
 * Provides subscription and unsubscription functionality for:
 * - Students data channel ("students-listener")
 * - History data channel ("history-listener")
 *
 * Uses a shared [CoroutineScope] for managing channel coroutines.
 */
object ChannelManager {
    // Shared CoroutineScope for hosting flows and channel subscriptions.
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // Channel listening for student-related realtime updates.
    private val studentsChannel = Channel(client.channel("students-listener"), "students", managerScope)
    // Channel listening for history-related realtime updates.
    private val historyChannel = Channel(client.channel("history-listener"), "history", managerScope)

    /**
     * Subscribes to the students channel.
     *
     * Before subscribing, unsubscribes from all other channels to avoid multiple active subscriptions.
     * Adds a small delay before subscribing to help with connection stability.
     *
     * @return A [Flow] emitting realtime [PostgresAction] events from the students channel.
     */
    suspend fun subscribeToStudentsChannel(): Flow<PostgresAction> {
        unsubscribeFromAllChannels()
        delay(10)
        Log.i("ChannelManager", "Attempting to connect to students channel")
        studentsChannel.subscribe()
        return studentsChannel.getFlow()
    }

    /**
     * Unsubscribes from all active channels (students and history).
     *
     * Useful to clean up active connections before opening new ones.
     */
    suspend fun unsubscribeFromAllChannels() {
        studentsChannel.unsubscribe()
        historyChannel.unsubscribe()
        Log.i("Database-ChannelManager", "Unsubscribed from all channels")
    }
}


