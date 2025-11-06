package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.loginSignUp.signUp.SignUpReq
import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.network.ApiInterface
import com.example.example.LoginReq

class LoginSignUpRepository(private val apiInterface: ApiInterface) {
    suspend fun login(req: LoginReq) = apiInterface.login(req)
    suspend fun signUp(req: SignUpReq) = apiInterface.signUp(req)
    suspend fun validateReferenceId(req: OtpSubmitReq) = apiInterface.validateReferenceId(req)

}