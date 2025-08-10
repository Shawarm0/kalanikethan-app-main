package com.lra.kalanikethan.data.models

import android.util.Log
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class Channel(
    private val channel: RealtimeChannel,
    private val tableName: String,
    managerScope: CoroutineScope // scope used for shareIn
) {
    private var listening = false

    // Create the upstream flow once (must happen before join)
    private val upstream: Flow<PostgresAction> =
        channel.postgresChangeFlow<PostgresAction>(schema = "public") { table = tableName }
            .onEach { action ->
                Log.d("Channel", "upstream emitted ($tableName): $action")
            }.shareIn(managerScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000), replay = 0)

    fun getFlow(): Flow<PostgresAction> = upstream


    suspend fun subscribe() {
        if (!listening) {
            try {
                channel.subscribe()
                listening = true
                Log.i("Models-Channel", "$tableName channel subscribed")
            } catch (e: Exception) {
                Log.e("Models-Channel", "Error while subscribing to $tableName channel : $e")
            }
        } else {
            Log.i("Models-Channel", "$tableName channel already subscribed")
        }
    }

    suspend fun unsubscribe() {
        if (listening) {
            try {
                channel.unsubscribe()
                listening = false
                Log.i("Models-Channel", "$tableName channel unsubscribed")
            } catch (e: Exception) {
                Log.e("Models-Channel", "Error while unsubscribing from $tableName channel : $e")
            }
        }
    }
}