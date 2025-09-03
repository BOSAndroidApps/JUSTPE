package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.forgotPassWord.ForgotReq
import com.bos.payment.appName.data.repository.ForgotPassWordRepository
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class ForgotPasswordViewModel(private val repository: ForgotPassWordRepository): ViewModel() {

    fun forgotPassword(req: ForgotReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.forgotPassword(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

}