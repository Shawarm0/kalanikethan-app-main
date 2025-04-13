package com.lra.kalanikethencic


import com.lra.kalanikethencic.ui.screens.Home


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lra.kalanikethencic.ui.components.KalanikethanAppDrawer
import com.lra.kalanikethencic.ui.components.TopAppBar
import com.lra.kalanikethencic.ui.screens.SignIn
import com.lra.kalanikethencic.ui.theme.KalanikethenCICTheme
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            KalanikethenCICTheme {
                // All for the Modal Navigation Drawer
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var selectedScreen by rememberSaveable { mutableStateOf("Home") }

                // For the top bar
                var icon by remember { mutableStateOf(Icons.Default.Home) }

                // For switching screens
                val navController = rememberNavController()

                when (selectedScreen) {
                    "Home" -> { icon = Icons.Outlined.Home }
                    "Sign In" -> { icon = Icons.Outlined.PersonAdd }
                    "Add" -> { icon = Icons.Outlined.AddCircleOutline }
                    "Who's In" -> { icon = Icons.Outlined.PersonSearch }
                    "History" -> { icon =  Icons.Filled.History }
                    "Payments "-> { icon = Icons.Default.Payments }
                    "Account" -> { icon = Icons.Default.AccountCircle }
                }

                // The Modal Navigation Drawer
                ModalNavigationDrawer(
                    drawerState = drawerState, // Default is closed
                    drawerContent = {
                        KalanikethanAppDrawer(
                            selectedScreen = selectedScreen,
                            onScreenSelected = { screenName ->
                                selectedScreen = screenName
                                scope.launch {
                                    drawerState.animateTo(DrawerValue.Closed, tween(500)) // Close the drawer
                                }
                                navController.navigate(screenName)
                            }
                        )
                    }
                ) {
                    Scaffold(topBar = {
                        TopAppBar(
                            icon = icon,
                            selectedScreen = selectedScreen,
                            title = selectedScreen,
                            scope = scope,
                            drawerState = drawerState,
                            onScreenSelected = { screenName ->
                                navController.navigate("Account")
                                selectedScreen = screenName
                            }
                        )
                    },
                        modifier = Modifier
                        .fillMaxSize().background(color = MaterialTheme.colorScheme.background)
                    ) { innerpadding ->
                            NavHost(navController, startDestination = "Home", modifier = Modifier.fillMaxSize().padding(innerpadding)) {
                                composable("Home") { Home() }
                                composable("Sign In") { SignIn() }
                                composable("Add") {  }
                                composable("Who's In") {  }
                                composable("History") {  }
                                composable("Payments") {  }
                                composable("Account") {  }
                            }

                    }

                }
            }
        }
    }
}