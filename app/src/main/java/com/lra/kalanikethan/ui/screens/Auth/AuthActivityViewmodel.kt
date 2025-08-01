package com.lra.kalanikethan.ui.screens.Auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.User
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


    fun LoginUser(userEmail: String, userPassword: String){
        viewModelScope.launch {
            try{
                auth.signInWith(Email){
                    email = userEmail
                    password = userPassword
                }
                userInfo = auth.retrieveUserForCurrentSession()
                Log.e("Auth", "Logged in")
            } catch (e: Exception){
                Log.e("Auth", "Login failed : $e")
            }
        }
    }

    fun retrieveNameFromUID(){
        viewModelScope.launch {
            val newUser : User = client.from("employees").select(columns = Columns.list("first_name, last_name, manager, uid")){
                filter {
                    User::uid eq userInfo.id
                }
            }.decodeSingle<User>()
            currentUser.value = newUser
        }
    }
}