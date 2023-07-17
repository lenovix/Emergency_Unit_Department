package com.kamilsudarmi.emergencyunit.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kamilsudarmi.emergencyunit.R
import com.kamilsudarmi.emergencyunit.api.ApiClient
import com.kamilsudarmi.emergencyunit.api.request.CallStatusRequest
import com.kamilsudarmi.emergencyunit.api.response.CallStatusResponse
import com.kamilsudarmi.emergencyunit.api.response.Panggilan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PanggilanAdapter(private val context: Context) : RecyclerView.Adapter<PanggilanAdapter.PanggilanViewHolder>() {

    private val panggilanList: MutableList<Panggilan> = mutableListOf()

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
            holder.mapsButton.visibility = View.GONE
            holder.acceptButton.setOnClickListener {
                Toast.makeText(context, "You Accepted this task, please refresh..", Toast.LENGTH_SHORT).show()
                updateCallStatus(panggilan.report_id.toString(), "Handled")
            }

            holder.acceptButton.text = "Accept"
        } else if (panggilan.status == "Handled") {
            holder.mapsButton.visibility = View.VISIBLE
            holder.acceptButton.setOnClickListener {
                Toast.makeText(context, "thank you for completing this task, please refresh..", Toast.LENGTH_SHORT).show()
                updateCallStatus(panggilan.report_id.toString(), "Completed")
            }
            holder.acceptButton.text = "Complete"
        }

        holder.mapsButton.setOnClickListener {
            val address = panggilan.address
            val latLong = panggilan.latlong
            if (panggilan.latlong == ""){
                val gmIntentUri = Uri.parse("geo:0,0?q=$address")
                Log.d("geo", "geo:0,0?q=$address")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    Toast.makeText(context, "Maps application is not installed", Toast.LENGTH_SHORT).show()
                }
            }else{
                val gmIntentUri = Uri.parse("geo:0,0?q=$latLong")
                Log.d("geo", "geo:0,0?q=$latLong")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    Toast.makeText(context, "Maps application is not installed", Toast.LENGTH_SHORT).show()
                }
            }
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

    fun setData(data: List<Panggilan>) {
        panggilanList.clear()
        panggilanList.addAll(data)
        notifyDataSetChanged()
    }

    inner class PanggilanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val latlongTextView: TextView = itemView.findViewById(R.id.latlongTextView)
        private val situationTextView: TextView = itemView.findViewById(R.id.situationTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val mapsButton: Button = itemView.findViewById(R.id.mapsButton)

        fun bindData(panggilan: Panggilan) {
            nameTextView.text = "Name: ${panggilan.nama_pengguna}"
            addressTextView.text = "Address: ${panggilan.address}"
            latlongTextView.text = "Latlong: ${panggilan.latlong}"
            situationTextView.text = "Situation: ${panggilan.situation}"
            statusTextView.text = "Status: ${panggilan.status}"
        }
    }
}
