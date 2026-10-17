package com.example.vaultlab.data

data class User(
    val username: String,
    val fullName: String,
    val accountNumber: String,
    val accountType: String,
    val routingNumber: String,
    val branchCode: String,
    val balance: Double
)
