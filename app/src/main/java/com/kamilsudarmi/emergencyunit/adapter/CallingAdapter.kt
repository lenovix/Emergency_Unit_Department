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
import com.kamilsudarmi.emergencyunit.api.response.Calling
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallingAdapter(private val context: Context) : RecyclerView.Adapter<CallingAdapter.CallingViewHolder>() {

    private val callingList: MutableList<Calling> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_call, parent, false
        )
        return CallingViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallingViewHolder, position: Int) {
        val calling = callingList[position]
        holder.bindData(calling)

        if (calling.status == "Pending") {
            holder.mapsButton.visibility = View.GONE
            holder.acceptButton.text = "Accept"
            holder.acceptButton.setOnClickListener {
                Toast.makeText(context, "You Accepted this task, please refresh..", Toast.LENGTH_SHORT).show()
                updateCallStatus(calling.report_id.toString(), "Handled")
            }
        } else if (calling.status == "Handled") {
            holder.mapsButton.visibility = View.VISIBLE
            holder.acceptButton.text = "Complete"
            holder.acceptButton.setOnClickListener {
                Toast.makeText(context, "thank you for completing this task, please refresh..", Toast.LENGTH_SHORT).show()
                updateCallStatus(calling.report_id.toString(), "Completed")
            }
        }

        holder.mapsButton.setOnClickListener {
            val address = calling.address
            val latLong = calling.latlong
            if (calling.latlong == ""){
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
        return callingList.size
    }

    fun setData(data: List<Calling>) {
        callingList.clear()
        callingList.addAll(data)
        notifyDataSetChanged()
    }

    inner class CallingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val latLongTextView: TextView = itemView.findViewById(R.id.latlongTextView)
        private val situationTextView: TextView = itemView.findViewById(R.id.situationTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val mapsButton: Button = itemView.findViewById(R.id.mapsButton)

        fun bindData(calling: Calling) {
            nameTextView.text = "Name: ${calling.nama_pengguna}"
            addressTextView.text = "Address: ${calling.address}"
            latLongTextView.text = "Lat-long: ${calling.latlong}"
            situationTextView.text = "Situation: ${calling.situation}"
            statusTextView.text = "Status: ${calling.status}"
        }
    }
}
