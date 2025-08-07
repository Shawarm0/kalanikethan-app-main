package com.lra.kalanikethan.ui.screens.Auth

import android.R.attr.top
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.data.models.User
import com.lra.kalanikethan.data.models.authCompleted
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.KalanikethanTheme
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import io.github.jan.supabase.auth.auth
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
    override fun onDestroy() {
        // Reset loading state when activity is destroyed
        super.onDestroy()
        model.loading = false
    }
}




@Composable
fun AuthMain(model: AuthActivityViewmodel) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val loggedin = authCompleted
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(Background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!loggedin) {
            SignedOut(
                email,
                password,
                model = model, // Pass the ViewModel
                onLogin = {
                    model.loginUser(email.value, password.value, context)
                }
            )
        } else {
            LoggedIn(
                sessionPermissions,
                model = model, // Pass the ViewModel
                onSignOut = {
                    model.signOutUser(context)
                    authCompleted = false
                }
            )
        }
    }
}




@Composable
fun LoggedIn(
    sessionPermissions: MutableState<User>,
    model: AuthActivityViewmodel, // Pass the ViewModel
    onSignOut: () -> Unit = {}
) {

    if (model.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 35.dp, start = 60.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Account Name: ${sessionPermissions.value.first_name} ${sessionPermissions.value.last_name}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = UnselectedButtonText
                    )
                )

                Text(
                    text = "Account Email: ${client.auth.currentSessionOrNull()?.user?.email}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = UnselectedButtonText
                    )
                )

                Text(
                    text = "Manager: ${sessionPermissions.value.manager}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = UnselectedButtonText
                    )
                )

                Button(
                    text = "Sign Out",
                    symbol = Icons.Default.Check,
                    onClick = { onSignOut() },
                    color = SuccessColor,
                    modifier = Modifier.width(173.dp)
                )
            }

        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignedOut(
    email: MutableState<String>,
    password: MutableState<String>,
    model: AuthActivityViewmodel, // Pass the ViewModel
    onLogin: () -> Unit = {}
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()


    if (model.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    } else {
        Spacer(
            modifier = Modifier.height(206.dp),
        )
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 40.sp,
                        lineHeight = 40.sp,
                        color = Color.Black
                    )
                )

                Text(
                    text = "Enter email and password to proceed",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = UnselectedButtonText
                    )

                )


            }

            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(25.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SimpleDecoratedTextField(
                    modifier = Modifier.height(60.dp)
                        .width(328.dp),
                    text = email.value,
                    placeholder = "test123@email.com",
                    label = "Email",
                    onValueChange = { email.value = it },
                    bringIntoViewRequester = bringIntoViewRequester,
                    coroutineScope = coroutineScope,
                )

                SimpleDecoratedTextField(
                    modifier = Modifier.height(60.dp)
                        .width(328.dp),
                    text = password.value,
                    placeholder = "••••••••••",
                    label = "Password",
                    onValueChange = { password.value = it },
                    trailingIcon2 = {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordHidden) "Show password" else "Hide password"
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    },
                    bringIntoViewRequester = bringIntoViewRequester,
                    coroutineScope = coroutineScope,
                    passwordHidden = passwordHidden
                )
            }


            Button(
                text = "Login",
                symbol = Icons.Default.Check,
                onClick = { onLogin() },
                color = SuccessColor,
                modifier = Modifier.width(173.dp)
            )
        }


    }

}