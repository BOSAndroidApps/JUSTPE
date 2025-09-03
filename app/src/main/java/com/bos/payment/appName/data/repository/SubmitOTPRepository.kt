package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.network.ApiInterface

class SubmitOTPRepository (private val apiInterface: ApiInterface) {

    suspend fun submitOTP(req: OtpSubmitReq) = apiInterface.submitOTP(req)

}