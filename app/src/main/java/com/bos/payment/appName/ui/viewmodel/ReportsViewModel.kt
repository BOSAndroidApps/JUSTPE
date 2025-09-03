package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportReq
import com.bos.payment.appName.data.repository.ReportsRepository
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class ReportsViewModel constructor(private val repository: ReportsRepository): ViewModel() {

    fun getAllRechargeAndBill(req: RechargeWiseReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllRechargeAndBill(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }
}