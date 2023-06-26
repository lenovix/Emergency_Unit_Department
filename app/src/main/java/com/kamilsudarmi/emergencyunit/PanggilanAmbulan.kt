package com.kamilsudarmi.emergencyunit

data class PanggilanAmbulan(
    val report_id: Int,
    val user_id: Int,
    val address: String,
    val situation: String,
    val unit: String,
    val status: String,
    val nama_pengguna: String
)