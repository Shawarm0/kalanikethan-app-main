package com.lra.kalanikethan.ui.screens.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.MainActivity
import com.lra.kalanikethan.data.models.User
import com.lra.kalanikethan.data.models.authCompleted
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.auth
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling user authentication and session state.
 *
 * Handles login, logout, and retrieval of user data from the Supabase database.
 */
class AuthActivityViewmodel : ViewModel() {

    /** Holds the currently logged-in user data. */
    val currentUser = mutableStateOf(User("None", "None", false, "None"))

    /** Stores information about the authenticated Supabase user. */
    lateinit var userInfo: io.github.jan.supabase.auth.user.UserInfo

    /** Indicates whether an authentication-related operation is in progress. */
    var loading by mutableStateOf(false)

    /**
     * Attempts to log in a user with the provided email and password.
     *
     * On successful login:
     * - Retrieves the authenticated user's information.
     * - Fetches additional details from the `employees` table.
     * - Updates session state and navigates to the [MainActivity].
     *
     * @param userEmail The user's email address.
     * @param userPassword The user's password.
     * @param context The [Context] used to start activities and finish the current one.
     */
    fun loginUser(userEmail: String, userPassword: String, context: Context) {
        loading = true
        viewModelScope.launch {
            try {
                Log.i("Auth - Viewmodel", "Attempting to login with email: $userEmail")

                // Authenticate with Supabase using email/password
                auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                // Retrieve the authenticated user's info
                userInfo = auth.retrieveUserForCurrentSession()
                Log.i("Auth - Viewmodel", "Logged in user ID: ${userInfo.id}")

            } catch (e: Exception) {
                Log.e("Auth - Viewmodel", "Login failed: $e")
                loading = false
                return@launch
            }

            try {
                Log.i("Auth - Viewmodel", "Retrieving user data from employees table")

                // Fetch employee details from the database
                val newUser: User = client.from("employees")
                    .select(columns = Columns.list("first_name, last_name, manager, uid")) {
                        filter {
                            User::uid eq userInfo.id
                        }
                    }
                    .decodeSingle()

                // Update current and session user data
                currentUser.value = newUser
                sessionPermissions.value = newUser

                // Navigate to main screen
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
                authCompleted = true

                Log.i("Auth - Viewmodel", "User data retrieved: ${newUser.first_name} ${newUser.last_name}, Manager: ${newUser.manager}")

            } catch (e: Exception) {
                Log.e("Auth - Viewmodel", "User data retrieval failed: $e")
            } finally {
                loading = false
            }
        }
    }

    /**
     * Signs the user out of the application and clears session state.
     *
     * @param context The [Context] used to finish the current activity.
     */
    fun signOutUser(context: Context) {
        loading = true
        viewModelScope.launch {
            try {
                auth.signOut(SignOutScope.GLOBAL)
                authCompleted = false
                sessionPermissions.value = User("None", "None", false, "")
                Log.i("Auth - Viewmodel", "User signed out successfully")
            } catch (e: Exception) {
                Log.e("Auth - Viewmodel", "Error during sign out: $e")
            } finally {
                (context as Activity).finish()
                loading = false
            }
        }
    }
}