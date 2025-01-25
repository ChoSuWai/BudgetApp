package com.example.budgettracker.screens

data class ExpenseTransaction(
    val category: String,
    val price: Double,
    val note: String,
    val date: String
)
