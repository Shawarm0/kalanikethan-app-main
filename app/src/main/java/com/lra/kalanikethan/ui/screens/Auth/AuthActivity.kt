package com.lra.kalanikethan.ui.screens.Auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.lra.kalanikethan.data.models.authCompleted
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.ui.theme.KalanikethanTheme
import kotlin.getValue

class AuthActivity : ComponentActivity() {
    val model: AuthActivityViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalanikethanTheme {
                AuthMain(model)
            }
        }
    }
}

@Composable
fun AuthMain(model: AuthActivityViewmodel){
    val currentUser by model.currentUser
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var username = remember { mutableStateOf("Not logged in yet") }
    val loggedin by authCompleted

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        if (!loggedin){
            Text("Sign in")
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = {Text("Email")},
                placeholder = {Text("Enter email here")},
                singleLine = true
            )

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = {Text("Password")},
                placeholder = {Text("Enter password here")},
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {passwordHidden = !passwordHidden}) {
                        val visibilityIcon = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                },
                visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            val context = LocalContext.current
            Button(onClick = {
                model.loginUser(email.value, password.value, context)
            }) {
                Text("Log in")
            }
        } else {
            Text("Current Account")
            Text("${sessionPermissions.value.first_name} ${sessionPermissions.value.last_name}")
            Text("Manager: ${sessionPermissions.value.manager}")
            val context = LocalContext.current
            Button(onClick = {model.signOutUser(context)}) {
                Text("Sign out")
            }
        }

    }


}