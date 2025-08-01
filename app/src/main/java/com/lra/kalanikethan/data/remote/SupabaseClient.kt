package com.lra.kalanikethan.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.cio.CIO

object SupabaseClientProvider {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://kfzzlelvlekrkduhgksy.supabase.co",
        supabaseKey = ,
    ) {
        install(Postgrest)
        install(Realtime)
        install(Auth)

        httpEngine = CIO.create()
    }

    val auth = client.auth
}