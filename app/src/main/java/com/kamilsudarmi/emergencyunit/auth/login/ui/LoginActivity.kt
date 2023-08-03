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
import com.kamilsudarmi.emergencyunit.auth.regiter.RegisterActivity
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
        binding.btnRegisterPage.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginMethod() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()

        val loginRequest = LoginRequest(username, password)
        Log.d("logininfo", "loginMethod: $username , $password")

        val call = ApiClient.ApiClient.apiService.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    val loginResponse = response.body()
                    val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("user_id", loginResponse?.user?.id.toString())
                    editor.putString("userName", loginResponse?.user?.username)
                    editor.putString("fullName", loginResponse?.user?.full_name)
                    editor.putString("departmentName", loginResponse?.user?.department_name)
                    editor.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = try {
                        val errorJson = JSONObject(errorResponse)
                        errorJson.getString("error")
                    } catch (e: JSONException) {
                        "There is an error. Please try again."
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