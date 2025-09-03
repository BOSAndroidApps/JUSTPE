package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.data.repository.SubmitOTPRepository
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class SubmitOTPViewModel(private val repository: SubmitOTPRepository): ViewModel() {

    fun submitOTP(req: OtpSubmitReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.submitOTP(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

}