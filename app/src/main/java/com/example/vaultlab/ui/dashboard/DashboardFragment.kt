package com.example.vaultlab.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaultlab.R
import com.example.vaultlab.data.SyntheticRepository
import com.example.vaultlab.ui.transactions.TransactionAdapter
import java.util.Locale

class DashboardFragment : Fragment() {

    interface OnViewAllClickListener {
        fun onViewAllTransactionsClicked()
    }

    var onViewAllClickListener: OnViewAllClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val context = requireContext()
        val repository = SyntheticRepository(context)

        val user = repository.getCurrentUser()
        val transactions = repository.getSyntheticTransactions()

        val tvGreeting: TextView = view.findViewById(R.id.tvGreeting)
        val tvAccountTypeNum: TextView = view.findViewById(R.id.tvAccountTypeNum)
        val tvBalanceAmount: TextView = view.findViewById(R.id.tvBalanceAmount)
        val btnViewAllTx: TextView = view.findViewById(R.id.btnViewAllTx)
        val rvRecentTransactions: RecyclerView = view.findViewById(R.id.rvRecentTransactions)

        tvGreeting.text = getString(R.string.greeting_format, user.fullName)
        tvAccountTypeNum.text = getString(R.string.tx_sub_format, user.accountType, user.accountNumber)
        tvBalanceAmount.text = String.format(Locale.US, "$%.2f", user.balance)

        val recentList = transactions.take(4)
        rvRecentTransactions.layoutManager = LinearLayoutManager(context)
        rvRecentTransactions.adapter = TransactionAdapter(recentList)

        btnViewAllTx.setOnClickListener {
            onViewAllClickListener?.onViewAllTransactionsClicked()
        }

        return view
    }
}
