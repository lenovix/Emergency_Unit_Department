package com.kamilsudarmi.emergencyunit.auth.login.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kamilsudarmi.emergencyunit.MainActivity
import com.kamilsudarmi.emergencyunit.R
import com.kamilsudarmi.emergencyunit.api.ApiClient
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginRequest
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginResponse
import com.kamilsudarmi.emergencyunit.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            loginMethod()
        }
    }

    private fun loginMethod() {
        // Mendapatkan input dari UI
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()

        // Membuat objek loginModel
        val loginRequest = LoginRequest(username, password)

        Log.d("logininfo", "loginMethod: $username , $password")

        // Memanggil metode login pada ApiService
        val call = ApiClient.ApiClient.apiService.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    // Respons berhasil
                    val loginResponse = response.body()
                    val token = loginResponse?.message
                    val user = loginResponse?.user

                    // Simpan status login sebagai true di SharedPreferences
                    val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("user_id", loginResponse?.user?.user_id)
                    editor.putString("userName", loginResponse?.user?.name)
                    editor.apply()

                    // Arahkan pengguna ke MainActivity atau tampilan utama lainnya
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Tutup LoginActivity agar pengguna tidak dapat kembali ke sini setelah login
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = try {
                        val errorJson = JSONObject(errorResponse)
                        errorJson.getString("error")
                    } catch (e: JSONException) {
                        "Terjadi kesalahan. Silakan coba lagi."
                    }
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("failure", "onFailure: $t")
            }
        })
    }
}