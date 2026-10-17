package com.example.vaultlab.data

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val amount: Double,
    val isCredit: Boolean,
    val status: String
)
