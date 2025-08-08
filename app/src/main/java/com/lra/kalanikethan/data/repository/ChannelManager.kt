package com.lra.kalanikethan.data.repository

import android.util.Log
import com.lra.kalanikethan.data.models.Channel
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.realtime.channel

class ChannelManager {

    var studentsChannel : Channel
    var historyChannel : Channel

    init {
        studentsChannel = Channel(client.channel("students-listener"), "students")
        historyChannel = Channel(client.channel("history-listener"), "history")
    }

    suspend fun subscribeToStudentsChannel() : Channel {
        Log.i("Database", "Attempting to connect to students channel")
        if (!studentsChannel.listening){
            try {
                studentsChannel.subscribe()
            } catch (e: Exception) {
                Log.e("Database", "Error occurred while attempting to subscribe to students channel : $e")
            }
        } else {
            Log.i("Database", "Students channel has already been subscribed too")
        }
        return studentsChannel
    }

    suspend fun subscribeToHistoryChannel() : Channel {
        if (!historyChannel.listening){
            try {
                historyChannel.subscribe()
                Log.i("Database", "Subscribed to history channel")
            } catch (e: Exception) {
                Log.e("Database", "Error occurred while attempting to subscribe to history channel : $e")
            }
        } else {
            Log.i("Database", "History channel has already been subscribed too")
        }
        return historyChannel
    }

    suspend fun unsubscribeFromAllChannels() {
        if (historyChannel.listening){
            historyChannel.unsubscribe()
        }
        if (studentsChannel.listening){
            studentsChannel.unsubscribe()
        }
        Log.i("Database", "Unsubscribed from all channels")
    }
}

val manager = ChannelManager()