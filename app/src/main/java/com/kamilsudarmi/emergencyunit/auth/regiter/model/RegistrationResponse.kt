package com.kamilsudarmi.emergencyunit.auth.regiter.model

import android.service.autofill.UserData

data class RegistrationResponse(
    val status: String,
    val message: String,
    val data: UserData // Contoh properti data, sesuaikan dengan respon aktual
)