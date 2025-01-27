package com.example.budgettracker.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.ExpenseTransaction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.log

@Composable
fun HomeScreen() {
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    // Manage the visibility of the overlay
    var showOverlay by remember { mutableStateOf(false) }

    // Manage the list of transactions
    val transactions = remember { mutableStateListOf<ExpenseTransaction>() }
    var isRefreshing by remember { mutableStateOf(false) }

    // Function to load transactions from Firebase
    val loadTransactions = suspend {
        try {
            db.collection("transactions").addSnapshotListener { result, error ->
                Log.d("firebase", ">>>>>>>>>>>>>>>")
                val loadedTransactions = result?.mapNotNull {
                    it.toObject(ExpenseTransaction::class.java).apply {
                        id = it.id // Store the document ID for deletion or updates
                    }
                }
                transactions.clear() // Clear the existing list to avoid duplicates
                loadedTransactions?.let { transactions.addAll(it) } // Add the latest data
            }

        } catch (e: Exception) {
            e.printStackTrace() // Log the error
        } finally {
            isRefreshing = false
        }
    }

    // Load transactions initially
    LaunchedEffect(Unit) {
        loadTransactions()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showOverlay = true }, // Show the overlay when FAB is clicked
                containerColor = MaterialTheme.colorScheme.primary, // Set background color to primary
                contentColor = Color.White, // Set icon color to white
                shape = CircleShape, // Ensure it's a circle
                modifier = Modifier.size(56.dp), // Adjust size if needed, the default size is already circular
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction",
                    )
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            /// Swipe-to-refresh
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    scope.launch { loadTransactions() }
                }
            ) {
                TransactionCards(transactions, db, scope)
            }

            // Conditionally show the AddScreenOverlay
            if (showOverlay) {
                AddScreenOverlay(
                    context = LocalContext.current,
                    onClose = { showOverlay = false }, // Close the overlay
                    onSaveTransaction = { transaction ->
                        scope.launch {
                            try {
                                val newTransactionRef =
                                    db.collection("transactions").add(transaction).await()
                                transaction.id =
                                    newTransactionRef.id // Assign the Firebase document ID
                                transactions.add(transaction) // Add the new transaction to the list locally
                                showOverlay = false
                            } catch (e: Exception) {
                                e.printStackTrace() // Log any errors
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionCards(
    transactions: MutableList<ExpenseTransaction>,
    db: FirebaseFirestore,
    scope: CoroutineScope
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(transactions) { transaction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Category: ${transaction.category}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Date: ${transaction.date}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Amount: \$${transaction.price}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Note: ${transaction.note}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Delete Button
                    IconButton(
                        onClick = {
                            scope.launch {
                                try {
                                    db.collection("transactions").document(transaction.id)
                                        .delete().await()
                                    transactions.remove(transaction) // Update list after deletion
                                } catch (e: Exception) {
                                    e.printStackTrace() // Log the error
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Transaction")
                    }
                }
            }
        }
    }
}
