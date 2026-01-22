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


/**
 * API key loaded from build configuration for secure access.
 */
private const val API_KEY = BuildConfig.KEY

/**
 * Singleton object providing configured Supabase client and auth utilities.
 */
object SupabaseClientProvider {


    /**
     * Initialized Supabase client with installed modules and HTTP engine.
     */
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://kfzzlelvlekrkduhgksy.supabase.co",
        supabaseKey = API_KEY,
    ) {
        install(Postgrest)
        install(Realtime)
        install(Auth) {
            autoLoadFromStorage = true
            alwaysAutoRefresh = true
        }


        httpEngine = CIO.create()
    }

    // Convenience reference to Supabase authentication module.
    val auth = client.auth

    /**
     * Checks if the currently authenticated user session is still valid.
     *
     * Queries the "employees" table to verify the existence of the user by UID.
     * If user data is found, updates sessionPermissions and sets authCompleted flag.
     *
     * @return True if user is valid and session data loaded successfully; false otherwise.
     */
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

                Log.i("Auth - SupabaseClientProvider", "User not found in database")
                return false

            } else {

                try {
                    sessionPermissions.value = result.decodeSingle<User>()
                    authCompleted = true
                } catch (e: Exception) {
                    Log.i("Auth - SupabaseClientProvider", "Error decoding single user: $e")
                }
                return true

            }
        } catch (e: Exception) {

            Log.i("Auth - SupabaseClientProvider", "Error decoding single user: $e")
            false

        }
    }
}