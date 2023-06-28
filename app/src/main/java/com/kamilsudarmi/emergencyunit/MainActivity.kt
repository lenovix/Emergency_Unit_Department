package com.kamilsudarmi.emergencyunit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kamilsudarmi.emergencyunit.api.ApiClient
import com.kamilsudarmi.emergencyunit.api.ApiService
import com.kamilsudarmi.emergencyunit.auth.login.ui.LoginActivity
import com.kamilsudarmi.emergencyunit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var panggilanAdapter: PanggilanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkLoginStatus()

        recyclerView = findViewById(R.id.callRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        panggilanAdapter = PanggilanAdapter()
        recyclerView.adapter = panggilanAdapter

        fetchDataFromApi()

        refreshApp()

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Hapus status login dari SharedPreferences
        val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Arahkan pengguna kembali ke LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Tutup MainActivity agar pengguna tidak dapat kembali ke sini setelah logout
    }

    private fun refreshApp() {
        val swipeToRefresh:SwipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener {
            fetchDataFromApi()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun fetchDataFromApi() {
        val apiService = ApiClient.ApiClient.apiService
        val call = apiService.getPanggilanAmbulan()

        call.enqueue(object : Callback<List<PanggilanAmbulan>> {
            override fun onResponse(
                call: Call<List<PanggilanAmbulan>>,
                response: Response<List<PanggilanAmbulan>>
            ) {
                if (response.isSuccessful) {
                    val panggilanList = response.body()
                    panggilanList?.let {
                        panggilanAdapter.setData(it)
                    }
                } else {
                    // Error handling
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch data: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<PanggilanAmbulan>>, t: Throwable) {
                // Error handling
                Toast.makeText(
                    this@MainActivity,
                    "Failed to fetch data: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val userName = sharedPreferences.getString("userName", "user")

        if (!isLoggedIn) {
            // Jika pengguna belum pernah login, arahkan ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: Tutup MainActivity agar pengguna tidak dapat kembali ke sini tanpa login
        } else {
            val welcomeMessage = "Welcome, $userName!"
            //binding.tvWelcome.text = welcomeMessage
        }
    }
}