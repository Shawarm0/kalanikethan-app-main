package com.lra.kalanikethencic.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethencic.ui.theme.AccentColor
import com.lra.kalanikethencic.ui.theme.ErrorColor
import com.lra.kalanikethencic.ui.theme.LightBoxBackground
import com.lra.kalanikethencic.ui.theme.PrimaryLightColor
import com.lra.kalanikethencic.ui.theme.SuccessColor
import com.lra.kalanikethencic.ui.theme.Typography
import java.time.LocalDate

@Composable
fun Payment(name: String = "Name", id: String = "AB123", price: Float = 0.00f, date: LocalDate?){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(color = LightBoxBackground)
        .size(1075.dp, 108.dp)
        .padding(12.dp)){
        Column {
            Row(modifier = Modifier.height(38.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(38.dp))
                Column {
                    Row(modifier = Modifier.width(168.dp)) {
                        Text(text = name,
                            style = Typography.titleSmall,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "View History",
                            modifier = Modifier.padding(top = 4.dp),
                            style = Typography.labelMedium,
                            color = PrimaryLightColor,
                            fontWeight = FontWeight.Normal)
                    }
                    Row(modifier = Modifier.width(168.dp)) {
                        Text(text = id, style = Typography.bodySmall, modifier = Modifier.weight(1.4f))
                        VerticalDivider(modifier = Modifier.weight(1f))
                        Text(text = "£" + price.toString(), style = Typography.bodySmall)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier
                    .size(117.dp, 30.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE7EEF5))
                    .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center){
                    Text(text = date.toString(), style = Typography.titleSmall)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.height(38.dp), verticalAlignment = Alignment.Bottom){
                //TODO: Once Database is ready we can figure out what to fill in the onClick
                Button("Confirm Payment", onClick = {}, color = SuccessColor)
                Spacer(modifier = Modifier.weight(1f))
                Button("Send Reminder", onClick = {}, color = AccentColor)
                Spacer(modifier = Modifier.weight(1f))
                Button("Incorrect Amount Paid", onClick = {}, color = ErrorColor)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(widthDp = 1075, heightDp = 108)
@Composable
fun PaymentInfo(){
    Payment("Alberry", date = LocalDate.of(2025, 4, 13))

}