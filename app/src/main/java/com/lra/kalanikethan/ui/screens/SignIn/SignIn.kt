package com.lra.kalanikethan.ui.screens.SignIn

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.github.jan.supabase.auth.SignOutScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun SignIn(viewModel: SignInViewModel) {
    LaunchedEffect(true) {
        viewModel.updateStudents(this)
    }
}