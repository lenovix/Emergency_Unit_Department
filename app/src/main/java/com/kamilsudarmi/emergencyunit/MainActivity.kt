package com.kamilsudarmi.emergencyunit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kamilsudarmi.emergencyunit.adapter.CallingAdapter
import com.kamilsudarmi.emergencyunit.api.ApiClient
import com.kamilsudarmi.emergencyunit.api.response.Calling
import com.kamilsudarmi.emergencyunit.auth.login.ui.LoginActivity
import com.kamilsudarmi.emergencyunit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar

    private lateinit var recyclerView: RecyclerView
    private lateinit var callingAdapter: CallingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        checkLoginStatus()

        recyclerView = findViewById(R.id.callRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        callingAdapter = CallingAdapter(this)
        recyclerView.adapter = callingAdapter

        val sharedPreferences: SharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("fullName", false.toString())
        val department = sharedPreferences.getString("departmentName", false.toString())
        binding.fullNameTextView.text = fullName
        binding.departmentNameTextView.text = department

        binding.titleTextView.text = "Task List for $department"
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
        val sharedPreferences: SharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val department = sharedPreferences.getString("departmentName", false.toString())
        swipeToRefresh.setOnRefreshListener {
            fetchDataFromApi()
            swipeToRefresh.isRefreshing = false
        }
    }
    private fun fetchDataFromApi() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val department = sharedPreferences.getString("departmentName", false.toString())
        val apiService = ApiClient.ApiClient.apiService
        val call = apiService.getPanggilan(department.toString())

        call.enqueue(object : Callback<List<Calling>> {
            override fun onResponse(
                call: Call<List<Calling>>,
                response: Response<List<Calling>>
            ) {
                if (response.isSuccessful) {
                    val panggilanList = response.body()
                    panggilanList?.let {
                        callingAdapter.setData(it)
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

            override fun onFailure(call: Call<List<Calling>>, t: Throwable) {
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