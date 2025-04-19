package com.lra.kalanikethencic


import android.os.Build
import com.lra.kalanikethencic.ui.screens.Home


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lra.kalanikethencic.ui.components.KalanikethanAppDrawer
import com.lra.kalanikethencic.ui.components.TopAppBar
import com.lra.kalanikethencic.ui.screens.Add
import com.lra.kalanikethencic.ui.screens.Payments
import com.lra.kalanikethencic.ui.screens.SignIn.SignIn
import com.lra.kalanikethencic.ui.screens.SignIn.SignInViewModel
import com.lra.kalanikethencic.ui.theme.KalanikethenCICTheme
import com.lra.kalanikethencic.util.imePaddingFraction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint // Required for hilt
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            KalanikethenCICTheme {

                // All for the Modal Navigation Drawer
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // For the top bar
                var icon by remember { mutableStateOf(Icons.Default.Home) }

                // For switching screens
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                var selectedScreen = navBackStackEntry?.destination?.route ?: "Dashboard"

                // This is to clear focus
                val focusManager = LocalFocusManager.current

                val signInViewModel: SignInViewModel = hiltViewModel()
                LaunchedEffect(Unit) {
                    signInViewModel.preloadStudents()
                }


                when (selectedScreen) {
                    "Dashboard" -> { icon = Icons.Outlined.Home }
                    "Sign In" -> { icon = Icons.Outlined.PersonAdd }
                    "Add" -> { icon = Icons.Outlined.AddCircleOutline }
                    "Who's In" -> { icon = Icons.Outlined.PersonSearch }
                    "History" -> { icon =  Icons.Filled.History }
                    "Payments" -> { icon = Icons.Default.Payment }
                    "Account" -> { icon = Icons.Default.AccountCircle }
                }


                // The Modal Navigation Drawer
                ModalNavigationDrawer(
                    drawerState = drawerState, // Default is closed
                    drawerContent = {
                        KalanikethanAppDrawer(
                            selectedScreen = selectedScreen,
                            onScreenSelected = { screenName ->
                                scope.launch {
                                    drawerState.animateTo(DrawerValue.Closed, tween(500)) // Close the drawer
                                }
                                navController.navigate(screenName) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                icon = icon,
                                selectedScreen = selectedScreen,
                                title = selectedScreen,
                                scope = scope,
                                drawerState = drawerState,
                                onScreenSelected = { screenName ->
                                    scope.launch {
                                        navController.navigate("Account") {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .clickable(
                                onClick = { focusManager.clearFocus() },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )


                    ) { innerpadding ->

                            NavHost(navController,
                                startDestination = "Dashboard",
                                modifier = Modifier
                                    .padding(innerpadding)
                                    .imePaddingFraction(0.9f) // Apply reduced bottom padding
                                ) {
                                composable("Dashboard") { Home() }
                                composable("Sign In") { SignIn() }
                                composable("Add") { Add() }
                                composable("Who's In") {  }
                                composable("History") {  }
                                composable("Payments") { Payments() }
                                composable("Account") {  }
                            }
                    }
                }
            }
        }
    }


    // So we know what's happening to the lifecycle of MainActivity.
    override fun onStart() {
        super.onStart()
        println("MainActivity: onStart() called")
    }

    override fun onResume() {
        super.onResume()
        println("MainActivity: onResume() called")
    }

    override fun onPause() {
        super.onPause()
        println("MainActivity: onPause() called")
    }

    override fun onStop() {
        super.onStop()
        println("MainActivity: onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity: onDestroy() called")
    }

    override fun onRestart() {
        super.onRestart()
        println("MainActivity: onRestart() called")
    }

}