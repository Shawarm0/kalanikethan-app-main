package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.Channel
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.realtime.channel

class ChannelManager {
    val signInChannel = Channel(client.channel("students-listener"), "students")

}