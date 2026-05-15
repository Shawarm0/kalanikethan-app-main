package com.lra.kalanikethan.data.remote

import android.util.Log
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel

object ChannelManager {

    private var studentsChannel: RealtimeChannel? = null
    private var historyChannel: RealtimeChannel? = null

    fun studentsChannel(): RealtimeChannel {
        return studentsChannel ?: client.channel("students-listener").also {
            studentsChannel = it
            Log.i("ChannelManager", "Created students channel")
        }
    }

    fun historyChannel(): RealtimeChannel {
        return historyChannel ?: client.channel("history-listener").also {
            historyChannel = it
            Log.i("ChannelManager", "Created history channel")
        }
    }

    suspend fun disconnectAll() {
        studentsChannel?.unsubscribe()
        historyChannel?.unsubscribe()
        Log.i("ChannelManager", "Disconnected all channels")
    }
}
