package com.example.budgettracker.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TransactionViewModel : ViewModel() {

    private val _transactions = mutableStateListOf<ExpenseTransaction>()
    val transactions: List<ExpenseTransaction> get() = _transactions

    // Add a transaction to the list
    fun addTransaction(transaction: ExpenseTransaction) {
        _transactions.add(transaction)
    }
}
