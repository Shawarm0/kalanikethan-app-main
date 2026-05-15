package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from

class HistoryRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getAllHistories(): List<History> {
        return client.from(Tables.HISTORY).select().decodeList<History>()
    }

    suspend fun queryNullHistories(): List<History> {
        return client.from(Tables.HISTORY).select().decodeList<History>().filter {
            it.signOutTime == null
        }
    }
}
