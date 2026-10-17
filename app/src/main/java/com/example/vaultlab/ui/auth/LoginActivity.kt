package com.example.vaultlab.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaultlab.MainActivity
import com.example.vaultlab.R
import com.example.vaultlab.data.AuthResult
import com.example.vaultlab.data.SyntheticRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var repository: SyntheticRepository
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvErrorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        repository = SyntheticRepository(this)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        when (repository.authenticate(username, password)) {
            AuthResult.SUCCESS_ADMIN -> {
                tvErrorMessage.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.m1_challenge_success),
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            AuthResult.SUCCESS_STANDARD -> {
                tvErrorMessage.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            AuthResult.FAILED -> {
                tvErrorMessage.text = getString(R.string.invalid_credentials)
                tvErrorMessage.visibility = View.VISIBLE
            }
        }
    }
}
