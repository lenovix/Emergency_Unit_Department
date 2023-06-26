package com.kamilsudarmi.emergencyunit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PanggilanAdapter : RecyclerView.Adapter<PanggilanAdapter.PanggilanViewHolder>() {

    private val panggilanList: MutableList<PanggilanAmbulan> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanggilanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_call, parent, false
        )
        return PanggilanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PanggilanViewHolder, position: Int) {
        val panggilan = panggilanList[position]
        holder.bindData(panggilan)
    }

    override fun getItemCount(): Int {
        return panggilanList.size
    }

    fun setData(data: List<PanggilanAmbulan>) {
        panggilanList.clear()
        panggilanList.addAll(data)
        notifyDataSetChanged()
    }

    inner class PanggilanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val idTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val situationTextView: TextView = itemView.findViewById(R.id.situationTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)

        fun bindData(panggilan: PanggilanAmbulan) {
            idTextView.text = "ID: ${panggilan.user_id}"
            addressTextView.text = "Address: ${panggilan.address}"
            situationTextView.text = "Situation: ${panggilan.situation}"
            statusTextView.text = "Status: ${panggilan.status}"
        }
    }
}
