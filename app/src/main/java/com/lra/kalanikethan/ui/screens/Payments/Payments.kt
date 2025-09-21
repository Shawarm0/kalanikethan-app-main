package com.lra.kalanikethan.ui.screens.Payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.ui.components.PaymentComponent
import com.lra.kalanikethan.ui.screens.Add.PaymentData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun Payments(
    paymentViewModel: PaymentViewModel,
    navController : NavHostController
) {
    val unpaidFamilies by paymentViewModel.unpaidFamilies.collectAsState(emptyList())
    val instant = Clock.System.now()
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    LaunchedEffect(true) {
        paymentViewModel.getUnpaidPayments()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        items(unpaidFamilies){ family ->
            val paymentData = PaymentData(
                paymentId = family.paymentPlan.family_payment_id,
                email = family.family.email,
                paymentDate = "${family.currentPayment.due_date.day}/${family.currentPayment.due_date.month.number}/${family.currentPayment.due_date.year}",
                familyName = family.family.familyName,
                amount = family.paymentPlan.amount.toString()
            )
            PaymentComponent(
                data = paymentData,
                onConfirmClick = {
                    paymentViewModel.confirmPayment(family.currentPayment.payment_id as Int, family.currentPayment.family_payment_id)
                },
                onViewHistoryClick = {
                    paymentViewModel.updateCurrentFamily(family.currentPayment.family_payment_id, family.family.familyName, family.paymentPlan.amount.toString())
                    navController.navigate(Screen.PaymentHistory.route)
                },
                overdue = localDate > family.currentPayment.due_date
            )
        }
    }
}