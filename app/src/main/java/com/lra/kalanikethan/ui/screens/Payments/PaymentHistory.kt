package com.lra.kalanikethan.ui.screens.Payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.ui.components.PaymentHistoryComponent
import com.lra.kalanikethan.util.groupHistoriesByDay
import com.lra.kalanikethan.util.groupPaymentHistoriesByMonth
import kotlinx.datetime.LocalDate


data class PaymentHistoryData (
    var history : List<PaymentHistory> = emptyList<PaymentHistory>(),
    var familyName : String = "",
    var familyID : String = "",
    var amount : String = "",
)

@Composable
fun PaymentHistory(
    viewModel: PaymentViewModel,
    navController : NavHostController
) {
    val data = viewModel.currentFamily.value
    val history = data.history
    val familyName = data.familyName
    val familyID = data.familyID

    Column(
        modifier = Modifier.fillMaxSize()

    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 12.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val groupedByMonth = groupPaymentHistoriesByMonth(history)

            items(1) {
                PaymentHistoryComponent(
                    data = history,
                    month = "${data.familyName} Family's Payment History"
                )
            }
        }
    }

}

@Composable
fun PaymentHistoryBox(
    due : LocalDate,
    amount : String,
    paid : Boolean
){
    Box(
        modifier = Modifier
            .width(1075.dp)
            .clip(RoundedCornerShape(12.dp))){
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(0.33f)) {
                Text("Date Due:")
                Text("$due")
            }
            Column(modifier = Modifier.fillMaxWidth(0.33f)) {
                Text("Amount:")
                Text(amount)
            }
            Column(modifier = Modifier.fillMaxWidth(0.33f)) {
                Text("Paid:")
                if (paid){
                    Text("YES", color = Color.Green)
                } else {
                    Text("NO", color = Color.Red)
                }
            }
        }
    }
}
