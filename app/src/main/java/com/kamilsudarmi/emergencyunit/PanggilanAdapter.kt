package com.kamilsudarmi.emergencyunit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamilsudarmi.emergencyunit.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // Set teks tombol tergantung dari status call
        if (panggilan.status == "Pending") {
            holder.acceptButton.setOnClickListener {
                // Mengirim permintaan untuk mengubah status panggilan ke "accepted"
                updateCallStatus(panggilan.report_id.toString(), "Handled")
            }
            holder.acceptButton.text = "Accept"
        } else if (panggilan.status == "Handled") {
            holder.acceptButton.setOnClickListener {
                // Mengirim permintaan untuk mengubah status panggilan ke "accepted"
                updateCallStatus(panggilan.report_id.toString(), "Completed")
            }
            holder.acceptButton.text = "Complete"
        }
    }

    private fun updateCallStatus(callId: String, newStatus: String) {
        val apiService = ApiClient.ApiClient.apiService
        val call = apiService.updateCallStatus(callId, CallStatusRequest(newStatus))
        call.enqueue(object : Callback<CallStatusResponse> {
            override fun onResponse(call: Call<CallStatusResponse>, response: Response<CallStatusResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    Log.d("API", "Status updated: $message")
                } else {
                    Log.e("API", "Failed to update status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CallStatusResponse>, t: Throwable) {
                Log.e("API", "Failed to update status: ${t.message}")
            }
        })
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

        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val situationTextView: TextView = itemView.findViewById(R.id.situationTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)

        fun bindData(panggilan: PanggilanAmbulan) {
            nameTextView.text = "Name: ${panggilan.nama_pengguna}"
            addressTextView.text = "Address: ${panggilan.address}"
            situationTextView.text = "Situation: ${panggilan.situation}"
            statusTextView.text = "Status: ${panggilan.status}"
        }
    }
}
