package com.example.budgettracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MonthlyScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "This is the Monthly Records content.", style = MaterialTheme.typography.bodyLarge)
    }
}