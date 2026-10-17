package com.example.vaultlab.data

import android.content.Context
import android.content.SharedPreferences

enum class AuthResult {
    SUCCESS_STANDARD,
    SUCCESS_ADMIN,
    FAILED
}

class SyntheticRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("vaultlab_session", Context.MODE_PRIVATE)

    companion object {
        const val PREF_LOGGED_IN = "is_logged_in"
        const val PREF_USERNAME = "current_username"

        // Default synthetic user accounts
        val DEMO_USER = User(
            username = "alex.morgan",
            fullName = "Alex Morgan",
            accountNumber = "ACC-8829-4102",
            accountType = "Premium Checking",
            routingNumber = "021000021",
            branchCode = "BR-704",
            balance = 12450.80
        )

        val SECONDARY_USER = User(
            username = "user1",
            fullName = "Jordan Smith",
            accountNumber = "ACC-1049-5831",
            accountType = "Standard Savings",
            routingNumber = "021000021",
            branchCode = "BR-704",
            balance = 3120.45
        )

        val ADMIN_USER = User(
            username = "admin",
            fullName = "System Administrator",
            accountNumber = "ACC-0000-0001",
            accountType = "Administrator Vault",
            routingNumber = "021000021",
            branchCode = "BR-000",
            balance = 999999.99
        )

        // Challenge M1: Obfuscated Admin Credentials for Static Analysis
        // NOTE: Byte array representation is intentionally used for Challenge M1 (Hardcoded Credentials).
        // This is simple encoding/obfuscation for educational reverse engineering (e.g. using JADX), NOT secure encryption.
        private val ADMIN_USER_BYTES = byteArrayOf(97, 100, 109, 105, 110) // "admin"
        private val ADMIN_PASS_BYTES = byteArrayOf(86, 97, 117, 108, 116, 65, 100, 109, 105, 110, 50, 48, 50, 53, 83, 101, 99, 114, 101, 116, 33) // "VaultAdmin2025Secret!"
    }

    fun authenticate(username: String, pass: String): AuthResult {
        val trimmedUser = username.trim().lowercase()
        val trimmedPass = pass.trim()

        if (trimmedUser.isEmpty() || trimmedPass.isEmpty()) {
            return AuthResult.FAILED
        }

        // Check standard training users
        if ((trimmedUser == "alex.morgan" && trimmedPass == "Vault2025!") ||
            (trimmedUser == "user1" && trimmedPass == "password123")
        ) {
            prefs.edit()
                .putBoolean(PREF_LOGGED_IN, true)
                .putString(PREF_USERNAME, trimmedUser)
                .apply()
            return AuthResult.SUCCESS_STANDARD
        }

        // Check obfuscated Admin credentials for Challenge M1
        val decodedAdminUser = String(ADMIN_USER_BYTES, Charsets.UTF_8)
        val decodedAdminPass = String(ADMIN_PASS_BYTES, Charsets.UTF_8)

        if (trimmedUser == decodedAdminUser && trimmedPass == decodedAdminPass) {
            prefs.edit()
                .putBoolean(PREF_LOGGED_IN, true)
                .putString(PREF_USERNAME, decodedAdminUser)
                .apply()
            return AuthResult.SUCCESS_ADMIN
        }

        return AuthResult.FAILED
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(PREF_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun getCurrentUser(): User {
        val current = prefs.getString(PREF_USERNAME, "alex.morgan")
        return when (current) {
            "user1" -> SECONDARY_USER
            "admin" -> ADMIN_USER
            else -> DEMO_USER
        }
    }

    fun getSyntheticTransactions(): List<Transaction> {
        return listOf(
            Transaction("TX-9001", "Global Tech Payroll", "Direct Deposit", "May 14, 2025", 4250.00, true, "Completed"),
            Transaction("TX-9002", "Artisan Coffee Roasters", "Dining", "May 13, 2025", 6.85, false, "Completed"),
            Transaction("TX-9003", "Metro Transit Authority", "Transportation", "May 12, 2025", 25.00, false, "Completed"),
            Transaction("TX-9004", "Organic Market", "Groceries", "May 11, 2025", 142.30, false, "Completed"),
            Transaction("TX-9005", "Electric Utility Corp", "Utilities", "May 09, 2025", 89.40, false, "Completed"),
            Transaction("TX-9006", "Peer-to-Peer Transfer", "Transfer", "May 07, 2025", 200.00, true, "Completed"),
            Transaction("TX-9007", "Cloud Subscriptions Inc", "Software", "May 05, 2025", 14.99, false, "Completed")
        )
    }

    fun resetSyntheticData() {
        prefs.edit().clear().apply()
    }
}
