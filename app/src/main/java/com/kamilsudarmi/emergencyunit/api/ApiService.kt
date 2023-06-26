package com.kamilsudarmi.emergencyunit.api

import com.kamilsudarmi.emergencyunit.PanggilanAmbulan
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginRequest
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/loginEU")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/panggilan-ambulan")
    fun getPanggilanAmbulan(): Call<List<PanggilanAmbulan>>
}