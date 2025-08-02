package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.util.topBorder

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    onTabSelected: (Tab) -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(Tab.Student) }

    Row(
        modifier = modifier
            .wrapContentSize()
            .background(Background)
            .topBorder(1.dp, Color(0xFFE5E8EB)),
        horizontalArrangement = Arrangement.Center
    ) {
        TabItem(
            tab = Tab.Student,
            isSelected = selectedTab == Tab.Student,
            modifier = if (selectedTab == Tab.Student) Modifier.topBorder(3.dp, AccentColor) else Modifier,
            onClick = { selectedTab = Tab.Student
            onTabSelected(Tab.Student)
            }
        )
        TabItem(
            tab = Tab.Parents,
            isSelected = selectedTab == Tab.Parents,
            modifier = if (selectedTab == Tab.Parents) Modifier.topBorder(3.dp, AccentColor) else Modifier,
            onClick = { selectedTab = Tab.Parents
            onTabSelected(Tab.Parents)
            }
        )
        TabItem(
            tab = Tab.Payments,
            isSelected = selectedTab == Tab.Payments,
            modifier = if (selectedTab == Tab.Payments) Modifier.topBorder(3.dp, AccentColor) else Modifier,
            onClick = { selectedTab = Tab.Payments
            onTabSelected(Tab.Payments)
            }
        )
    }
}

enum class Tab(val icon: ImageVector, val title: String) {
    Student(
        icon = Icons.Default.Person,
        title = "Students"
    ),
    Parents(
        icon = Icons.Default.Group,
        title = "Parents"
    ),
    Payments(
        icon = Icons.Default.Payment,
        title = "Payments"
    )
}

@Composable
fun TabItem(
    tab: Tab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.width(312.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.title,
                tint = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tab.title,
                color = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}