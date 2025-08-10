package com.lra.kalanikethan.data.remote

import android.util.Log
import com.lra.kalanikethan.BuildConfig
import com.lra.kalanikethan.data.models.User
import com.lra.kalanikethan.data.models.authCompleted
import com.lra.kalanikethan.data.models.sessionPermissions
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

const val apiKey = BuildConfig.KEY
object SupabaseClientProvider {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://kfzzlelvlekrkduhgksy.supabase.co",
        supabaseKey = apiKey,
    ) {
        install(Postgrest)
        install(Realtime)
        install(Auth)

        httpEngine = CIO.create()
    }

    val auth = client.auth
    suspend fun isUserStillValid(): Boolean {
        return try {
            val user = client.auth.currentUserOrNull()?.id ?: return false

            val result = client.from("employees")
                .select() {
                    filter {
                        User::uid eq user
                    }
                }

            if (result.data == "[]") {
                return false
            } else {

                try {
                    sessionPermissions.value = result.decodeSingle<User>()
                    authCompleted = true
                } catch (e: Exception) {
                    Log.i("Auth", "Error decoding single user: $e")
                }

                return true
            }

        } catch (e: Exception) {
            false
        }
    }


}