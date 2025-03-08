package com.system.lsp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhoneNumbersAdapter(
    private val phoneNumbers: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PhoneNumbersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val phoneNumber = phoneNumbers[position]
        holder.textView.text = phoneNumber
        holder.itemView.setOnClickListener { onItemClick(phoneNumber) }
    }

    override fun getItemCount() = phoneNumbers.size
}
