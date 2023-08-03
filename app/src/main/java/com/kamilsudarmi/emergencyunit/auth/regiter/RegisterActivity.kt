package com.kamilsudarmi.emergencyunit.auth.regiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.kamilsudarmi.emergencyunit.MainActivity
import com.kamilsudarmi.emergencyunit.api.ApiClient.ApiClient.apiService
import com.kamilsudarmi.emergencyunit.auth.login.ui.LoginActivity
import com.kamilsudarmi.emergencyunit.auth.regiter.model.RegistrationResponse
import com.kamilsudarmi.emergencyunit.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            registerMethod()
        }

    }

    private fun registerMethod() {
        val fullName = binding.fullNameEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val selectedDepartmentId = binding.departmentRadioGroup.checkedRadioButtonId
        if (selectedDepartmentId != -1) {
            val selectedRadioButton = binding.departmentRadioGroup.findViewById<RadioButton>(selectedDepartmentId)
            val selectedDepartment = selectedRadioButton.text.toString()
            registerUser(fullName, username, password, selectedDepartment)
        } else {
            Toast.makeText(this, "Choose a department first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(fullName: String, username: String, password: String, selectedDepartment: String) {
        val call = apiService.registerUser(fullName, username, password, selectedDepartment)
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}