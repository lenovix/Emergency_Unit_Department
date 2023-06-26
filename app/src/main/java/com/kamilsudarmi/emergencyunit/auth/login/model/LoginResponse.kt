package com.kamilsudarmi.emergencyunit.auth.login.model

data class LoginResponse(
    val message: String,
    val user: User
)

data class User(
    val user_id: String,
    val name: String,
    val email: String,
    val password: String
)