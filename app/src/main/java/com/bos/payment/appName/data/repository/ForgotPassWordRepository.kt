package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.forgotPassWord.ForgotReq
import com.bos.payment.appName.network.ApiInterface

class ForgotPassWordRepository(private val apiInterface: ApiInterface) {
    suspend fun forgotPassword(req: ForgotReq) = apiInterface.forgotPassword(req)
}