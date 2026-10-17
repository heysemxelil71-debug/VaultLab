package com.example.vaultlab.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.vaultlab.R
import com.example.vaultlab.data.SyntheticRepository

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val context = requireContext()
        val repository = SyntheticRepository(context)
        val user = repository.getCurrentUser()

        val tvHolderName: TextView = view.findViewById(R.id.tvHolderName)
        val tvAccountType: TextView = view.findViewById(R.id.tvAccountType)
        val tvAccountNumber: TextView = view.findViewById(R.id.tvAccountNumber)
        val tvRoutingNumber: TextView = view.findViewById(R.id.tvRoutingNumber)
        val tvBranchCode: TextView = view.findViewById(R.id.tvBranchCode)

        tvHolderName.text = user.fullName
        tvAccountType.text = user.accountType
        tvAccountNumber.text = user.accountNumber
        tvRoutingNumber.text = user.routingNumber
        tvBranchCode.text = user.branchCode

        return view
    }
}
