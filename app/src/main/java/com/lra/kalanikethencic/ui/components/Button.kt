package com.lra.kalanikethencic.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.theme.ButtonColor

@Composable
fun Button(text: String, symbol: ImageVector? = null, onClick: () -> Unit){
    Box (modifier = Modifier
        .clip(RoundedCornerShape(15.dp))
        .background(color = ButtonColor)
        .height(30.dp)
        .padding(horizontal = 10.dp)
        .wrapContentSize(Alignment.Center)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
    ) {
        Row (verticalAlignment = Alignment.CenterVertically){
            if(symbol != null){
                Icon(symbol, contentDescription = "Symbol", modifier = Modifier.size(20.dp), tint = Color.White)
            }
            Text(text = text, style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFFFFF))
        }
    }
}

@Preview
@Composable
fun ButtonInfo(){
    Button("Monkeys", Icons.Default.Add, {})
}

