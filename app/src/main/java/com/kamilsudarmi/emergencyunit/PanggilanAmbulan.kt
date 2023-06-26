package com.kamilsudarmi.emergencyunit

data class PanggilanAmbulan(
    val id: Int,
    val user_id: Int,
    val address: String,
    val situation: String,
    val unit: String,
    val status: String
)