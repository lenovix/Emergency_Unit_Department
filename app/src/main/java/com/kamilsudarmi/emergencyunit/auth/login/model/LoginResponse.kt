package com.kamilsudarmi.emergencyunit.auth.login.model

data class LoginResponse(
    val message: String,
    val user: User
)
data class User(
    val id: Int,
    val full_name: String,
    val username: String,
    val password: String,
    val department_name: String,
)