package com.bos.payment.appName.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCReq
import com.bos.payment.appName.data.model.kyc.UpdateKYCReq
import com.bos.payment.appName.data.model.pinChange.ChangePasswordReq
import com.bos.payment.appName.data.model.pinChange.PinChangeReq
import com.bos.payment.appName.data.model.serviceWiseTrans.ServiceWiseTransactionReq
import com.bos.payment.appName.data.repository.ServiceChangeRepository
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class ServiceChangeViewModel constructor(private val repository: ServiceChangeRepository): ViewModel() {

    fun changePin(req: PinChangeReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.pinChange(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun serviceWise(req: ServiceWiseTransactionReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.serviceWise(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun updateKYC(req: UpdateKYCReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            Log.d(TAG, "updateKYC: ${req.EmailID}")
            Log.d(TAG, "updateKYC: ${req.City}")
            Log.d(TAG, "updateKYC: ${req.State}")
            Log.d(TAG, "updateKYC: ${req.FirstName}")
            Log.d(TAG, "updateKYC: ${req.HoldAmt}")
            emit(ApiResponse.success(data = repository.updateKYC(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }
    fun reteriveData(req: ReteriveAgentKYCReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.reteriveData(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun changePassword(req: ChangePasswordReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.changePassword(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }
    fun getState() = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getState()))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }
}