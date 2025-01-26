package com.example.budgettracker

import SavingScreen
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.budgettracker.screens.HomeScreen
import com.example.budgettracker.screens.MonthlyScreen
import com.example.budgettracker.screens.AccountScreen
import com.example.budgettracker.screens.LoginScreen
import com.example.budgettracker.screens.RegisterScreen
import com.example.budgettracker.ui.theme.BudgetTrackerTheme
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val auth = FirebaseAuth.getInstance()
    val navController = rememberNavController()
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val context = LocalContext.current.applicationContext
    val currentScreenTitle = remember { mutableStateOf("Overview") }

    // ExportFile function
    val onExportFile: () -> Unit = {
        Toast.makeText(context, "Export File Later.", Toast.LENGTH_SHORT)
            .show() //TODO remove appname in Toast message
    }

    // Help function
    val onHelp: () -> Unit = {
        Toast.makeText(context, "Help Later.", Toast.LENGTH_SHORT).show()
    }

    // Notification function
    val onNotification: () -> Unit = {
        Toast.makeText(context, "Notification Later.", Toast.LENGTH_SHORT).show()
    }

    // About Us function
    val onAboutUs: () -> Unit = {
        Toast.makeText(context, "Developed by Cho Su Wai.", Toast.LENGTH_SHORT).show()
    }

    // Logout function
    val onLogout: () -> Unit = {
        auth.signOut()
        isUserLoggedIn = false

        // Clear SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear().apply()

        navController.navigate("login") {
            popUpTo("account") { inclusive = true }
        }
    }

    // Delete Account function
    val onDeleteAccount: () -> Unit = {
        val user = auth.currentUser
        user?.let {
            val credential = EmailAuthProvider.getCredential(
                user.email ?: "",
                "userPassword"
            ) // Replace with actual password
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            isUserLoggedIn = false
                            navController.navigate("login") {
                                popUpTo("account") { inclusive = true }
                            }
                        } else {
                            Log.e("Account", "Account deletion failed: ${task.exception?.message}")
                        }
                    }
                } else {
                    Log.e("Account", "Re-authentication failed: ${reauthTask.exception?.message}")
                }
            }
        }
    }

    // Set screen title based on navigation route
    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentScreenTitle.value = when (destination.route) {
            "home" -> "Overview"
            "calendar" -> "Monthly Records"
            "saving" -> "Saving"
            "account" -> "Your Account"
            else -> "Overview"
        }
    }

    Scaffold(
        topBar = {
            if (isUserLoggedIn) {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentScreenTitle.value,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.Black, // Set text color to black
                                    textAlign = TextAlign.Center, // Center align text
                                    fontSize = 18.sp
                                )
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        bottomBar = if (isUserLoggedIn) {
            { BottomNavigationBar(navController) }
        } else {
            {}
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) "home" else "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate("register") {
                            popUpTo("login") { inclusive = false }
                        }
                    },
                    onNavigateToHome = {
                        isUserLoggedIn = true
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        isUserLoggedIn = true
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") { HomeScreen() }
            composable("calendar") { MonthlyScreen() }
            composable("saving") { SavingScreen() }
            composable("account") {
                AccountScreen(
                    onExportFile = onExportFile,
                    onHelp = onHelp,
                    onNotification = onNotification,
                    onAboutUs = onAboutUs,
                    onLogout = onLogout,
                    onDeleteAccount = onDeleteAccount
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController) {
    val currentRoute = navController.currentDestination?.route
    val title = when (currentRoute) {
        "home" -> "Overview"
        "calendar" -> "Monthly Record"
        "saving" -> "Saving"
        "account" -> "Your Account"
        else -> ""
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.ic_home),
        BottomNavItem("calendar", R.drawable.ic_calendar),
        BottomNavItem("saving", R.drawable.ic_saving),
        BottomNavItem("account", R.drawable.ic_account)
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(painter = painterResource(id = item.icon),
                    contentDescription = null) },
                alwaysShowLabel = false
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: Int
)