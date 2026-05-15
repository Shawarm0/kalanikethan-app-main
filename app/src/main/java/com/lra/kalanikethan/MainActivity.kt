package com.lra.kalanikethan


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.repository.AttendanceRepository
import com.lra.kalanikethan.data.repository.ClassRepository
import com.lra.kalanikethan.data.repository.EmployeeRepository
import com.lra.kalanikethan.data.repository.FamilyRepository
import com.lra.kalanikethan.data.repository.HistoryRepository
import com.lra.kalanikethan.data.repository.PaymentRepository
import com.lra.kalanikethan.data.repository.StudentRepository
import com.lra.kalanikethan.ui.components.KalanikethanAppDrawer
import com.lra.kalanikethan.ui.components.TopAppBar
import com.lra.kalanikethan.ui.screens.Add.Add
import com.lra.kalanikethan.ui.screens.Add.AddViewModel
import com.lra.kalanikethan.ui.screens.Auth.AuthActivity
import com.lra.kalanikethan.ui.screens.history.History
import com.lra.kalanikethan.ui.screens.Payments.PaymentViewModel
import com.lra.kalanikethan.ui.screens.Payments.Payments
import com.lra.kalanikethan.ui.screens.Payments.PaymentHistory
import com.lra.kalanikethan.navigation.Screen
import com.lra.kalanikethan.ui.screens.whosein.WhoseIn
import com.lra.kalanikethan.ui.screens.dashboard.Classes
import com.lra.kalanikethan.ui.screens.dashboard.Dashboard
import com.lra.kalanikethan.ui.screens.dashboard.EditClass
import com.lra.kalanikethan.ui.screens.editfamily.EditFamily
import com.lra.kalanikethan.ui.screens.editfamily.EditFamilyViewModel
import com.lra.kalanikethan.ui.screens.signIn.SignIn
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.KalanikethanTheme
import com.lra.kalanikethan.util.imeBottomPaddingFraction
import com.lra.kalanikethan.viewmodel.AppViewModelFactory
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.viewmodel.ClassManagementViewModel
import com.lra.kalanikethan.viewmodel.HistoryViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val factory by lazy {
        AppViewModelFactory(
            studentRepository = StudentRepository(),
            attendanceRepository = AttendanceRepository(),
            classRepository = ClassRepository(),
            historyRepository = HistoryRepository(),
            employeeRepository = EmployeeRepository(),
            familyRepository = FamilyRepository(),
            paymentRepository = PaymentRepository()
        )
    }

    private val attendanceVM: AttendanceViewModel by viewModels { factory }
    private val classVM: ClassManagementViewModel by viewModels { factory }
    private val historyVM: HistoryViewModel by viewModels { factory }
    private val addVM: AddViewModel by viewModels { factory }
    private val paymentVM: PaymentViewModel by viewModels { factory }
    private val editFamilyVM: EditFamilyViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KalanikethanTheme {

                val sessionStatus by client.auth.sessionStatus.collectAsState()

                LaunchedEffect(sessionStatus) {
                    when (sessionStatus) {
                        is SessionStatus.Authenticated -> {
                            SupabaseClientProvider.isUserStillValid()
                        }

                        is SessionStatus.NotAuthenticated -> {
                            val intent = Intent(this@MainActivity, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        else -> Unit
                    }
                }

                when (sessionStatus) {
                    is SessionStatus.Authenticated -> {
                        KalanikethanApp(
                            attendanceVM = attendanceVM,
                            classVM = classVM,
                            historyVM = historyVM,
                            addVM = addVM,
                            paymentVM = paymentVM,
                            editFamilyVM = editFamilyVM
                        )
                    }

                    SessionStatus.Initializing -> {
                        FullscreenLoader()
                    }

                    else -> {}
                }
            }
        }
    }

}



@Composable
fun KalanikethanApp(
    attendanceVM: AttendanceViewModel,
    classVM: ClassManagementViewModel,
    historyVM: HistoryViewModel,
    addVM: AddViewModel,
    paymentVM: PaymentViewModel,
    editFamilyVM: EditFamilyViewModel
) {
    LaunchedEffect(Unit) {
        classVM.bindAllStudents(attendanceVM.allStudents)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route
    var selectedIcon by remember { mutableStateOf(Screen.Dashboard.filledIcon) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            KalanikethanAppDrawer(
                selectedScreen = selectedScreen,
                onScreenSelected = { newScreen ->
                    scope.launch {
                        @Suppress("DEPRECATION")
                        drawerState.animateTo(DrawerValue.Closed, tween(500))
                    }
                    navController.navigate(newScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    selectedIcon = newScreen.filledIcon
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    icon = selectedIcon,
                    selectedScreen = selectedScreen,
                    title = selectedScreen,
                    scope = scope,
                    drawerState = drawerState,
                    onAccountSelected = {
                        val intent = Intent(context, AuthActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Background)
                .clickable(
                    onClick = { focusManager.clearFocus() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(innerPadding)
                    .imeBottomPaddingFraction(0.9f),
            ) {
                composable(route = Screen.Dashboard.route) {
                    Dashboard(classVM, navController, selectedIconChange = { icon ->
                        selectedIcon = icon
                    })
                }
                composable(route = Screen.SignIn.route) {
                    SignIn(attendanceVM, onEditFamily = { familyId ->
                        navController.navigate(Screen.EditFamily.createRoute(familyId))
                    })
                }
                composable(route = Screen.Add.route) { Add(addVM) }
                composable(route = Screen.WhoseIn.route) {
                    WhoseIn(attendanceVM, onEditFamily = { familyId ->
                        navController.navigate(Screen.EditFamily.createRoute(familyId))
                    })
                }
                composable(route = Screen.History.route) { History(historyVM, attendanceVM, classVM) }
                composable(route = Screen.Account.route) { }
                composable(route = Screen.Class.route) {
                    Classes(classVM, attendanceVM, onEditFamily = { familyId ->
                        navController.navigate(Screen.EditFamily.createRoute(familyId))
                    })
                }
                composable(route = Screen.EditClass.route) { EditClass(classVM, attendanceVM, navController) }
                composable(route = Screen.Payments.route) { Payments(paymentVM, navController) }
                composable(route = Screen.PaymentHistory.route) { PaymentHistory(paymentVM, navController) }
                composable(
                    route = Screen.EditFamily.route,
                    arguments = listOf(navArgument("familyId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val familyId = backStackEntry.arguments?.getString("familyId") ?: return@composable
                    EditFamily(editFamilyVM, familyId, onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}


@Composable
fun FullscreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
