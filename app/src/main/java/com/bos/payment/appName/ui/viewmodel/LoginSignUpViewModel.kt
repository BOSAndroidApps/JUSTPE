package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.loginSignUp.signUp.SignUpReq
import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.data.repository.LoginSignUpRepository
import com.bos.payment.appName.utils.ApiResponse
import com.bumptech.glide.load.engine.Resource
import com.example.example.LoginReq
import kotlinx.coroutines.Dispatchers

class LoginSignUpViewModel (private val repository: LoginSignUpRepository): ViewModel() {


    fun login(req: LoginReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.login(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun signUp(req: SignUpReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.signUp(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun validateReferenceId(req: OtpSubmitReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.validateReferenceId(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

}