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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen(
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
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
            ProfileSection("Guest")

            Spacer(modifier = Modifier.height(24.dp))

            AccountMenu(
                onExportFile = { /* Handle export file logic */ },
                onHelp = { /* Handle help logic */ },
                onNotification = { /* Handle notification settings */ },
                onAboutUs = { /* Handle about us logic */ },
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
fun ProfileSection(username: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image (Optional, placeholder for now)
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display user name
        Text(
            text = username,
            style = MaterialTheme.typography.bodySmall,
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
        MenuItem("Export file", Icons.Default.FileDownload, onExportFile),
        MenuItem("Help", Icons.Default.Help, onHelp),
        MenuItem("Notification", Icons.Default.Notifications, onNotification),
        MenuItem("About Us", Icons.Default.Info, onAboutUs),
        MenuItem("Log out", Icons.Default.ExitToApp, onLogout),
        MenuItem("Delete Account", Icons.Default.Delete, onDeleteAccount)
    )

    // Menu list
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        items(menuItems) { item ->
            MenuOptionRow(item.label, item.icon, item.onClick)
        }
    }
}

@Composable
fun MenuOptionRow(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Go",
            modifier = Modifier.size(20.dp)
        )
    }
}

data class MenuItem(val label: String, val icon: ImageVector, val onClick: () -> Unit)
