package com.system.lsp.ui.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.system.lsp.R
import com.system.lsp.domain.LoanSummary

class HomeCustomerAdapter(
    private val listener: OnItemClickListener,
    private var summaryList: List<LoanSummary>
) : RecyclerView.Adapter<HomeCustomerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(loanSummary: LoanSummary)
        fun showDocumentPhoto(document: String)
        fun showPhoneBottomSheet(phoneList: List<String>)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val date: TextView = v.findViewById(R.id.date)
        val customerName: TextView = v.findViewById(R.id.customerName)
        val fee: TextView = v.findViewById(R.id.fee)
        val address: TextView = v.findViewById(R.id.address)
        val totalAmount: TextView = v.findViewById(R.id.totalAmount)
        val customerId: TextView = v.findViewById(R.id.customerId)
        val loanId: TextView = v.findViewById(R.id.loanId)
        val statusIndicator: View = itemView.findViewById(R.id.statusIndicator)
        val cardLayout: CardView = itemView.findViewById(R.id.cardLayout)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val phoneIcon: ImageView = itemView.findViewById(R.id.phoneIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.due_quote_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loanSummary = summaryList[position]

        val statusIndicator = holder.statusIndicator
        statusIndicator.setBackgroundResource(
            if (loanSummary.totalExpiredQuotas == 1) R.color.colorPrimary else R.color.mora
        )

        holder.fee.text = loanSummary.totalExpiredQuotas.toString()
        holder.customerName.text = loanSummary.customerName
        holder.customerId.text = loanSummary.customerDocumentId
        holder.address.text = loanSummary.customerAddress
        holder.date.text = loanSummary.dueDate
        holder.totalAmount.text = "RD$ ${loanSummary.totalAmountToPay}"
        holder.loanId.text = "Prestamo : ${loanSummary.loanId}"

        holder.cardLayout.setOnClickListener {
            listener.onClick(loanSummary)
        }

        holder.photo.setOnClickListener {
            listener.showDocumentPhoto(loanSummary.customerDocumentId)
        }

        holder.phoneIcon.setOnClickListener {
            listener.showPhoneBottomSheet(listOf())
        }
    }


    override fun getItemCount(): Int = summaryList.size
}
