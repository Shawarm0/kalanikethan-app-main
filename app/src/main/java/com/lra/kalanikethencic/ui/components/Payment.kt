package com.lra.kalanikethencic.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import java.time.format.DateTimeFormatter

@SuppressLint("WeekBasedYear")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Payment(
    name: String = "Family Name",
    id: String = "AB123",
    price: Float = 0.00f,
    date: LocalDate?
){
    // Put the date into british format
    val formattedDate = date?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    // Background Box
    Box(
        modifier = Modifier
        // This is just for the shadow effect
        .shadow(
            elevation = 4.dp, // Figma blur roughly maps to elevation
            shape = RoundedCornerShape(0.dp), // or your desired shape
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        // Clip the contents to be round
        .clip(RoundedCornerShape(12.dp))
        .background(color = LightBoxBackground)
        .size(1075.dp, 108.dp)
        .padding(12.dp)
    ) {
        // This is the Content Column
        Column {
            // Top Row
            Row(
                modifier = Modifier
                    .height(38.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(38.dp))
                    Spacer(modifier = Modifier.width(5.dp))

                    Column {
                        Row(
                            modifier = Modifier.width(168.dp)
                        ) {

                            Text(
                                text = name,
                                style = Typography.titleSmall,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "View History",
                                modifier = Modifier.padding(top = 4.dp)
                                    .clickable(
                                        onClick = { println("View History Clicked") },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                style = Typography.labelMedium,
                                color = PrimaryLightColor,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        // This is the row below the Name and view History
                        Row(
                            modifier = Modifier
                                .width(168.dp)
                        ) {
                            Text(
                                text = id,
                                style = Typography.bodySmall,
                                modifier = Modifier.weight(1.4f)
                            )
                            VerticalDivider(modifier = Modifier.weight(1f))
                            Text(text = "£$price" + "0", style = Typography.bodySmall)
                        }

                    }
                }

                Box(
                    modifier = Modifier
                    .size(117.dp, 30.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE7EEF5))
                    .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formattedDate.toString(),
                        style = Typography.titleSmall
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .height(38.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //TODO: Once Database is ready we can figure out what to fill in the onClick
                Button("Confirm Payment", onClick = {}, color = SuccessColor)
                Button("Send Reminder", onClick = {}, color = AccentColor)
                Button("Incorrect Amount Paid", onClick = {}, color = ErrorColor)
            }
        }
    }
}