package com.lra.kalanikethan.ui.screens.Auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lra.kalanikethan.ui.theme.KalanikethanTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalanikethanTheme {

            }
        }
    }
}