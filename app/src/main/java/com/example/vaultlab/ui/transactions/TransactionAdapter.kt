package com.example.vaultlab.ui.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vaultlab.R
import com.example.vaultlab.data.Transaction
import java.util.Locale

class TransactionAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTxTitle: TextView = itemView.findViewById(R.id.tvTxTitle)
        val tvTxSub: TextView = itemView.findViewById(R.id.tvTxSub)
        val tvTxAmount: TextView = itemView.findViewById(R.id.tvTxAmount)
        val tvTxStatus: TextView = itemView.findViewById(R.id.tvTxStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = transactions[position]
        val context = holder.itemView.context

        holder.tvTxTitle.text = item.title
        holder.tvTxSub.text = context.getString(R.string.tx_sub_format, item.category, item.date)
        holder.tvTxStatus.text = item.status

        if (item.isCredit) {
            holder.tvTxAmount.text = String.format(Locale.US, "+$%.2f", item.amount)
            holder.tvTxAmount.setTextColor(ContextCompat.getColor(context, R.color.green_credit))
        } else {
            holder.tvTxAmount.text = String.format(Locale.US, "-$%.2f", item.amount)
            holder.tvTxAmount.setTextColor(ContextCompat.getColor(context, R.color.red_debit))
        }
    }

    override fun getItemCount(): Int = transactions.size
}
