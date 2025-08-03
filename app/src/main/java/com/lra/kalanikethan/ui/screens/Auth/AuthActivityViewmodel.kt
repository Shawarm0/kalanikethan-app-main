package com.lra.kalanikethan.ui.screens.Auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.User
import com.lra.kalanikethan.data.models.authCompleted
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class AuthActivityViewmodel: ViewModel() {
    val currentUser = mutableStateOf(User("None", "None", false, "None"))
    lateinit var userInfo : UserInfo

    fun loginUser(userEmail: String, userPassword: String, context : Context){
        viewModelScope.launch {
            try{
                Log.i("Auth", "Attempting to Login with email: $userEmail, password: $userPassword")
                auth.signInWith(Email){
                    email = userEmail
                    password = userPassword
                }
                userInfo = auth.retrieveUserForCurrentSession()
                Log.i("Auth", "Logged in ${userInfo.id}")
            } catch (e: Exception){
                Log.e("Auth", "Login failed : $e")
            }
            try {
                Log.i("Auth", "Retrieving user data")
                var newUser : User = client.from("employees").select(columns = Columns.list("first_name, last_name, manager, uid")){
                    filter {
                        User::uid eq userInfo.id
                    }
                }.decodeSingle<User>()
                currentUser.value = newUser
                sessionPermissions.value = newUser
                (context as Activity).finish()
                Log.i("Auth", "User data retrieved - First name: ${newUser.first_name}, Last name: ${newUser.last_name}, Manager: ${newUser.manager}")
            } catch (e: Exception){
                Log.e("Auth", "User data retrieval failed : $e")
            }
        }
    }
}