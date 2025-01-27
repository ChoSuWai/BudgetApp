package com.example.budgettracker.data

// Data class for transaction
data class ExpenseTransaction(
    var id: String = "", // Document ID for Firebase
    val category: String = "",
    val price: Double = 0.0,
    val note: String = "",
    val date: String = "")
