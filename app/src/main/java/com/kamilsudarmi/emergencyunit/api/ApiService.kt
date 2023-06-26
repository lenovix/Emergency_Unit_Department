package com.kamilsudarmi.emergencyunit.api

import com.kamilsudarmi.emergencyunit.PanggilanAmbulan
import com.kamilsudarmi.emergencyunit.CallStatusRequest
import com.kamilsudarmi.emergencyunit.CallStatusResponse
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginRequest
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/loginEU")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/panggilan-ambulan")
    fun getPanggilanAmbulan(): Call<List<PanggilanAmbulan>>

    @PUT("calls/{report_id}")
    fun updateCallStatus(@Path("report_id") reportId: String, @Body body: CallStatusRequest): Call<CallStatusResponse>
}