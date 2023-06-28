package com.kamilsudarmi.emergencyunit.api

import com.kamilsudarmi.emergencyunit.api.response.Panggilan
import com.kamilsudarmi.emergencyunit.api.request.CallStatusRequest
import com.kamilsudarmi.emergencyunit.api.response.CallStatusResponse
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginRequest
import com.kamilsudarmi.emergencyunit.auth.login.model.LoginResponse
import com.kamilsudarmi.emergencyunit.auth.regiter.model.RegistrationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/loginEU")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("/panggilan/{department}")
    fun getPanggilan(
        @Path("department") department: String
    ): Call<List<Panggilan>>
    @PUT("/calls/{report_id}")
    fun updateCallStatus(@Path("report_id") reportId: String, @Body body: CallStatusRequest): Call<CallStatusResponse>

    @FormUrlEncoded
    @POST("/register_department")
    fun registerUser(
        @Field("full_name") fullName: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("department_name") departmentName: String
    ): Call<RegistrationResponse>
}