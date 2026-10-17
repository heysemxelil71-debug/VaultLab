package com.example.vaultlab.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaultlab.R
import com.example.vaultlab.data.SyntheticRepository

class TransactionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)
        val context = requireContext()
        val repository = SyntheticRepository(context)

        val transactions = repository.getSyntheticTransactions()
        val rvAllTransactions: RecyclerView = view.findViewById(R.id.rvAllTransactions)

        rvAllTransactions.layoutManager = LinearLayoutManager(context)
        rvAllTransactions.adapter = TransactionAdapter(transactions)

        return view
    }
}
