package com.bos.payment.appName.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bos.payment.appName.data.model.justpaymodel.CheckBankDetailsModel
import com.bos.payment.appName.data.model.justpaymodel.GenerateVirtualAccountModel
import com.bos.payment.appName.data.model.justpaymodel.RetailerContactListRequestModel
import com.bos.payment.appName.data.model.justpaymodel.SendMoneyToMobileReqModel
import com.bos.payment.appName.data.model.justpaymodel.UpdateBankDetailsReq
import com.bos.payment.appName.data.model.justpedashboard.RetailerWiseServicesRequest
import com.bos.payment.appName.data.model.menuList.GetAllMenuListReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.mobileCharge.GetCommercialReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileWiseRechargeReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeCategoryReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeOperatorsReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargePlanReq
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryReq
import com.bos.payment.appName.data.model.travel.flight.GetAirTicketListReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.IOException

class GetAllApiServiceViewModel constructor(private val repository: GetAllAPIServiceRepository) :
    ViewModel() {

    fun getAllRechargeAndBillServiceCharge(req: GetCommercialReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllRechargeAndBillServiceCharge(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getAllMerchantBalance(req: GetMerchantBalanceReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllMerchantBalance(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getWalletBalance(req: GetBalanceReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getWalletBalance(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getTransferAmountToAgents(req: TransferAmountToAgentsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getTransferAmountToAgents(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getAllApiPayoutCommercialCharge(req: GetPayoutCommercialReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllApiPayoutCommercialCharge(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getAllMenuList(req: GetAllMenuListReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllMenuList(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getAllTransactionReport(req: TransactionReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAllTransactionReport(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    // for air requery request

    fun getAirRequeryRequest(req: FlightRequeryReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_0000) { // 10 seconds timeout
                repository.getAirRequeryRequest(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "Something went wrong: ${e.localizedMessage}"
                )
            )
        }
    }


    // for air ticket request
    fun getAirTicketListRequest(req: GetAirTicketListReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_000) { // 100 seconds (1 minute 40 seconds)
                repository.getAirTicketListRequest(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "Something went wrong: ${e.localizedMessage}"
                )
            )
        }
    }


    //dashboard banner
    fun getDashboardBannerRequest(rid: Int, retailercode: String, task: String) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_000) {
                repository.getDashboardBanner(rid, retailercode, task)
            }

            if (response.isSuccessful) {
                emit(ApiResponse.success(response.body()))
            } else {
                emit(ApiResponse.error(data = null, message = "Server error: ${response.code()}"))
            }

        } catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        } catch (e: IOException) {
            emit(ApiResponse.error(data = null, message = "No internet connection. Please check your network."))
        } catch (e: Exception) {
            emit(ApiResponse.error(data = null, message = "Something went wrong: ${e.localizedMessage}"))
        }
    }


    // services request for retailerwise
    fun getRetailerWiseServicesReq(req: RetailerWiseServicesRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerWiseServicesRequest(req)))
            }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
            }

    }


    fun getBankDetails(req: CheckBankDetailsModel) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_0000) { // 10 seconds timeout
                repository.getBankDetails(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "Something went wrong: ${e.localizedMessage}"
                )
            )
        }
    }



    fun updateBankDetails(req: UpdateBankDetailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_0000) { // 10 seconds timeout
                repository.updateBankDetails(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "Something went wrong: ${e.localizedMessage}"
                )
            )
        }
    }



    fun getRetailerContactList(req: RetailerContactListRequestModel) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_0000) { // 10 seconds timeout
                repository.getRetailerContactList(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "Something went wrong: ${e.localizedMessage}"
                )
            )
        }
    }



    fun sendMoneyToMobileReqModel(req: SendMoneyToMobileReqModel) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = withTimeout(10_0000) { // 10 seconds timeout
                repository.sendMoneyToMobileReqModel(req)
            }
            emit(ApiResponse.success(response))
        }
        catch (e: TimeoutCancellationException) {
            emit(ApiResponse.error(data = null, message = "Request timed out. Please try again."))
        }
        catch (e: IOException) {
            emit(
                ApiResponse.error(
                    data = null,
                    message = "No internet connection. Please check your network."
                )
            )
        }
        catch (e: Exception) {
            emit(ApiResponse.error(data = null, message = "Something went wrong: ${e.localizedMessage}"))
        }
    }


}