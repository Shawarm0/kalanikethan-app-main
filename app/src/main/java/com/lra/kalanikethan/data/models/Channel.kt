package com.lra.kalanikethan.data.models

import android.util.Log
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow

data class Channel(
    val channel: RealtimeChannel,
    val tableName: String,
    var listening: Boolean = false
) {
    private val dataFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public"){
        table = tableName
    }
    fun getFlow() : Flow<PostgresAction> {
        return dataFlow
    }
    suspend fun subscribe(){
        try{
            channel.subscribe()
            listening = true
            Log.i("Database", "$tableName channel subscribed")
        } catch (e: Exception){
            Log.e("Database", "Error while subscribing to $tableName channel : $e")
        }

    }
    suspend fun unsubscribe(){
       try{
           channel.unsubscribe()
           listening = false
           Log.i("Database", "$tableName channel unsubscribed")
       } catch (e: Exception){
           Log.e("Database", "Error while unsubscribing from $tableName channel : $e")
       }

    }
}