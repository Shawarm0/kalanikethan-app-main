package com.lra.kalanikethan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lra.kalanikethan.ui.components.PaymentComponent
import com.lra.kalanikethan.ui.screens.Add.PaymentData
import io.github.jan.supabase.realtime.Column

@Composable
fun Payments() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentComponent(
            data = PaymentData(
                familyName = "Family Name",
                paymentId = "NV14LLG",
                amount = "12.00"
            ),
        )
    }
}