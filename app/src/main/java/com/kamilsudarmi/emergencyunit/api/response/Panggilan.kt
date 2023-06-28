package com.kamilsudarmi.emergencyunit.api.response

data class Panggilan(
    val report_id: Int,
    val user_id: Int,
    val address: String,
    val situation: String,
    val unit: String,
    val status: String,
    val nama_pengguna: String
)