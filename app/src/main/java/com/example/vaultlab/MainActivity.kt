package com.example.vaultlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vaultlab.data.SyntheticRepository
import com.example.vaultlab.ui.account.AccountFragment
import com.example.vaultlab.ui.auth.LoginActivity
import com.example.vaultlab.ui.dashboard.DashboardFragment
import com.example.vaultlab.ui.settings.SettingsFragment
import com.example.vaultlab.ui.transactions.TransactionsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), DashboardFragment.OnViewAllClickListener {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = SyntheticRepository(this)
        if (!repository.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    showDashboardFragment()
                    true
                }
                R.id.navigation_transactions -> {
                    loadFragment(TransactionsFragment())
                    true
                }
                R.id.navigation_account -> {
                    loadFragment(AccountFragment())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            showDashboardFragment()
        }
    }

    private fun showDashboardFragment() {
        val fragment = DashboardFragment()
        fragment.onViewAllClickListener = this
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onViewAllTransactionsClicked() {
        bottomNavigation.selectedItemId = R.id.navigation_transactions
    }
}
