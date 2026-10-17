package com.example.vaultlab.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vaultlab.R
import com.example.vaultlab.data.SyntheticRepository
import com.example.vaultlab.ui.auth.LoginActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val context = requireContext()
        val repository = SyntheticRepository(context)

        val btnResetData: Button = view.findViewById(R.id.btnResetData)
        val btnSignOut: Button = view.findViewById(R.id.btnSignOut)

        btnResetData.setOnClickListener {
            repository.resetSyntheticData()
            Toast.makeText(context, getString(R.string.data_reset_success), Toast.LENGTH_SHORT).show()
        }

        btnSignOut.setOnClickListener {
            repository.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
