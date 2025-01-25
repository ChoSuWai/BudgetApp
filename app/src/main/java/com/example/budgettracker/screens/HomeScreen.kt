package com.example.budgettracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    // Manage the visibility of the overlay
    var showOverlay by remember { mutableStateOf(false) }

    // Manage the list of transactions
    var transactions by remember { mutableStateOf(listOf<ExpenseTransaction>()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showOverlay = true }, // Show the overlay when FAB is clicked
                content = { Icon(Icons.Default.Add, contentDescription = "Add Transaction") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Show the list of transactions as cards
            TransactionCards(transactions)

            // Conditionally show the AddScreenOverlay
            if (showOverlay) {
                AddScreenOverlay(
                    onClose = { showOverlay = false }, // Close the overlay
                    onSaveTransaction = { transaction ->
                        transactions = transactions + transaction // Add the new transaction to the list
                        showOverlay = false // Close the overlay after saving
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionCards(transactions: List<ExpenseTransaction>) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        transactions.forEach { transaction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Date: ${transaction.date}", style = MaterialTheme.typography.bodyLarge)
                    Text("Amount: ${transaction.price}", style = MaterialTheme.typography.bodyLarge)
                    Text("Note: ${transaction.note}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
