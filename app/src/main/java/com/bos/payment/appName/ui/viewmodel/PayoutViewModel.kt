package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutReq
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountReq
import com.bos.payment.appName.data.model.recharge.payout.payoutBos.PayoutAppReq
import com.bos.payment.appName.data.model.recharge.payout.payoutStatus.PayoutStatusReq
import com.bos.payment.appName.data.repository.PayoutRepository
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class PayoutViewModel constructor(private val repository: PayoutRepository) :
    ViewModel() {

    val apiInterface = RetrofitClient.apiInterface

    fun sendAmount(req: PayoutAmountReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val result = repository.sendAmount(req)
            if (result!!.body() != null) {
                if (result?.body()?.data != null) {
                    emit(ApiResponse.success(data = result))
                } else {
                    emit(
                        ApiResponse.error(
                            data = null,
                            message = result?.body()?.message ?: "Error Occurred!"
                        )
                    )
                }
            } else {
                emit(
                    ApiResponse.error(
                        data = null,
                        message = result.message() ?: "Error Occurred!"
                    )
                )
            }

        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getPayoutStatus(req: PayoutStatusReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getPayoutStatus(req)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getPayoutAmountTransaction(req: PayoutAppReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {

            val result = repository.getPayoutAmountTransaction(req)
            if (result.body() != null) {
                if (result.body()?.data != null) {
                    emit(ApiResponse.success(data = result))
                } else {
                    emit(
                        ApiResponse.error(
                            data = null,
                            message = result?.body()?.message ?: "Error Occurred!"
                        )
                    )
                }
            } else {
                emit(
                    ApiResponse.error(
                        data = null,
                        message = result.message() ?: "Error Occurred!"
                    )
                )
            }
//            emit(ApiResponse.success(data = repository.getPayoutAmountTransaction(req)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun sendAllPayoutAmount(req: AOPPayOutReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {

            val result = repository.sendAllPayoutAmount(req)
            if (result?.body() != null) {
                if (result.body()?.initiateAuthGenericFundTransferAPIResp?.metaData != null) {
                    emit(ApiResponse.success(data = result))
                } else {
                    emit(
                        ApiResponse.error(
                            data = null,
                            message = result?.body()?.message ?: "Error Occurred!"
                        )
                    )
                }
            } else {
                emit(
                    ApiResponse.error(
                        data = null,
                        message = result?.message() ?: "Error Occurred!"
                    )
                )
            }
//            emit(ApiResponse.success(data = repository.getPayoutAmountTransaction(req)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}