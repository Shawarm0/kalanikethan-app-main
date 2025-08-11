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

/**
 * Represents a subscription to a real-time Postgres change channel for a specific table.
 *
 * This class wraps a [RealtimeChannel] and provides:
 * - A shared [Flow] of [PostgresAction] changes for the table.
 * - Methods to subscribe and unsubscribe from the channel.
 * - Automatic logging for emitted events and subscription changes.
 *
 * The flow is shared using [shareIn] to ensure multiple consumers can observe
 * the same upstream changes without re-subscribing to the channel.
 *
 * @property channel The underlying [RealtimeChannel] instance used for listening to table changes.
 * @property tableName The name of the Postgres table to listen to.
 * @constructor Creates a new [Channel] bound to the given table and channel.
 *
 * @param managerScope The [CoroutineScope] used for sharing the flow across multiple collectors.
 */
class Channel(
    private val channel: RealtimeChannel,
    private val tableName: String,
    managerScope: CoroutineScope
) {
    private var listening = false

    /**
     * A shared flow of [PostgresAction] changes for this table.
     *
     * The upstream flow is created before joining the channel to avoid missing events.
     * It logs every received action for debugging purposes.
     */
    private val upstream: Flow<PostgresAction> =
        channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = tableName
        }
            .onEach { action ->
                Log.d("Channel", "[$tableName] upstream emitted: $action")
            }
            .shareIn(
                scope = managerScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                replay = 0
            )


    // Returns the shared flow of table change events.
    fun getFlow(): Flow<PostgresAction> = upstream

    /**
     * Subscribes to the real-time channel for this table, if not already subscribed.
     *
     * Logs the subscription state and any errors encountered.
     */
    suspend fun subscribe() {
        if (listening) {
            Log.i("Models-Channel", "[$tableName] already subscribed")
            return
        }

        try {
            channel.subscribe()
            listening = true
            Log.i("Models-Channel", "[$tableName] channel subscribed")
        } catch (e: Exception) {
            Log.e("Models-Channel", "Error subscribing to [$tableName]: $e")
        }
    }

    /**
     * Unsubscribes from the real-time channel for this table, if currently subscribed.
     *
     * Logs the unsubscription state and any errors encountered.
     */
    suspend fun unsubscribe() {
        if (!listening) return

        try {
            channel.unsubscribe()
            listening = false
            Log.i("Models-Channel", "[$tableName] channel unsubscribed")
        } catch (e: Exception) {
            Log.e("Models-Channel", "Error unsubscribing from [$tableName]: $e")
        }
    }
}