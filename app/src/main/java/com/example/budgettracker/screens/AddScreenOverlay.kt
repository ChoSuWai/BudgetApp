package com.example.budgettracker.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.ExpenseTransaction
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

@Composable
fun AddScreenOverlay(
    context: Context,
    onClose: () -> Unit,
    onSaveTransaction: (ExpenseTransaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val db = FirebaseFirestore.getInstance()
    var selectedCategory by remember { mutableStateOf("Choose Category") }
    var selectedDate by remember { mutableStateOf("Pick a Date") }
    var note by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val categories = listOf("Restaurants", "Transportation", "Saving", "Groceries", "Shopping")

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Close Button
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close Overlay")
                }
            }

            // Dropdown Menu for Categories
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(selectedCategory)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar for Date Selection
            OutlinedButton(
                onClick = {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    android.app.DatePickerDialog(
                        context,
                        { _, selectedYear, selectedMonth, selectedDay ->
                            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        },
                        year,
                        month,
                        day
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedDate)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price TextField
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Note TextField
            TextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    if (selectedCategory != "Choose Category" && price.isNotEmpty() && selectedDate != "Pick a Date") {
                        val transaction = ExpenseTransaction(
                            category = selectedCategory,
                            price = price.toDouble(),
                            note = note,
                            date = selectedDate
                        )
                        onSaveTransaction(transaction)

                        // Save to Firebase
//                        db.collection("transactions")
//                            .add(transaction)
//                            .addOnSuccessListener {
//                                // Successfully saved
//                            }
//                            .addOnFailureListener {
//                                // Handle error
//                            }

                        onClose()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
