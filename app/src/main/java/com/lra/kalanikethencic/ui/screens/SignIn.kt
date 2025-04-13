package com.lra.kalanikethencic.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.components.Button
import com.lra.kalanikethencic.ui.components.SelectionButton

@Composable
fun SignIn(){
    Column {
        Button("Hello", Icons.Default.Add)
        Spacer(modifier = Modifier.height(5.dp))
        SelectionButton("Hello")
    }
}