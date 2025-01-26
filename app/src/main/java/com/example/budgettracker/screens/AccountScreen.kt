package com.example.budgettracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.budgettracker.R
import com.example.budgettracker.ui.theme.robotoFontFamily

@Composable
fun AccountScreen(
    onExportFile: () -> Unit,
    onHelp: () -> Unit,
    onNotification: () -> Unit,
    onAboutUs: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val username = remember { mutableStateOf("Guest") }

    LaunchedEffect(Unit) {
        username.value = sharedPreferences.getString("username", "Guest") ?: "Guest"
    }

    val isSigningOut = remember { mutableStateOf(false) }

    if (isSigningOut.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ProfileSection(
                username.value,
                profileImageRes = R.drawable.profile_image
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccountMenu(
                onExportFile = onExportFile,
                onHelp = onHelp,
                onNotification = onNotification,
                onAboutUs = onAboutUs,
                onLogout = {
                    isSigningOut.value = true
                    onLogout()
                },
                onDeleteAccount = { onDeleteAccount() }
            )
        }
    }
}


@Composable
fun ProfileSection(username: String, profileImageRes: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Load profile image from resource
        Image(
            painter = painterResource(id = profileImageRes), // Use a drawable resource
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Profile Image (Placeholder Icon)
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = "Profile Icon",
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primary)
//                    .padding(16.dp),
//                tint = Color.White
//            )

        //Spacer(modifier = Modifier.height(4.dp))

        // Display user name
        Text(
            text = username,
            style = TextStyle(fontFamily = robotoFontFamily, fontSize = 18.sp),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun AccountMenu(
    onExportFile: () -> Unit,
    onHelp: () -> Unit,
    onNotification: () -> Unit,
    onAboutUs: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val menuItems = listOf(
        MenuItem("Export file", R.drawable.ic_export_file, onExportFile, false),
        MenuItem("Help", R.drawable.ic_help, onHelp, false),
        MenuItem("Notification", R.drawable.ic_notification, onNotification, false),
        MenuItem("About Us", R.drawable.ic_about_us, onAboutUs, false),
        MenuItem("Log out", R.drawable.ic_log_out, onLogout, true),
        MenuItem("Delete Account", R.drawable.ic_delete_account, onDeleteAccount, true)
    )

    // Menu list
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        items(menuItems) { item ->
            MenuOptionRow(item.label, item.iconRes, item.onClick)
        }
    }
}

@Composable
fun MenuOptionRow(
    label: String,
    iconRes: Int,
    onClick: () -> Unit,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Load icon from resources using painterResource
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.padding(start = 16.dp, end = 24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


data class MenuItem(
    val label: String,
    val iconRes: Int,
    val onClick: () -> Unit,
    val showArrow: Boolean
)
