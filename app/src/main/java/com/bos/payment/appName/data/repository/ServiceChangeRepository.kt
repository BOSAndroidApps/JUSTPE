package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCReq
import com.bos.payment.appName.data.model.kyc.UpdateKYCReq
import com.bos.payment.appName.data.model.pinChange.ChangePasswordReq
import com.bos.payment.appName.data.model.pinChange.PinChangeReq
import com.bos.payment.appName.data.model.serviceWiseTrans.ServiceWiseTransactionReq
import com.bos.payment.appName.network.ApiInterface

class ServiceChangeRepository(private val apiInterface: ApiInterface) {
    
    suspend fun pinChange(req: PinChangeReq) = apiInterface.changePin(req)
    suspend fun serviceWise(req: ServiceWiseTransactionReq) = apiInterface.serviceWiseReports(req)
    suspend fun updateKYC(req: UpdateKYCReq) = apiInterface.updateKYC(req)
    suspend fun reteriveData(req: ReteriveAgentKYCReq) = apiInterface.reterieveKYCDetails(req)
    suspend fun changePassword(req: ChangePasswordReq) = apiInterface.changePassword(req)
    suspend fun getState() = apiInterface.getState()
//    suspend fun getReDirectUrl(req: RedirectUrlVerifyReq) = apiInterface.getReDirectUrl(req)
//    suspend fun getAllMerchantList(req: GetApiListMarchentWiseReq) = apiInterface.getAllMerchantList(req)
}