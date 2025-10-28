package com.bos.payment.appName.data.repository

import com.bos.payment.appName.data.model.justpaymodel.CheckBankDetailsModel
import com.bos.payment.appName.data.model.justpaymodel.GenerateVirtualAccountModel
import com.bos.payment.appName.data.model.justpaymodel.RetailerContactListRequestModel
import com.bos.payment.appName.data.model.justpaymodel.SendMoneyToMobileReqModel
import com.bos.payment.appName.data.model.justpaymodel.UpdateBankDetailsReq
import com.bos.payment.appName.data.model.justpedashboard.DashboardBannerListModel
import com.bos.payment.appName.data.model.justpedashboard.RetailerWiseServicesRequest
import com.bos.payment.appName.data.model.menuList.GetAllMenuListReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.mobileCharge.GetCommercialReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileWiseRechargeReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeCategoryReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeOperatorsReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargePlanReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeViewPlanResponse
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryReq
import com.bos.payment.appName.data.model.travel.flight.GetAirTicketListReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.google.android.gms.common.api.Response
import okhttp3.RequestBody

class GetAllAPIServiceRepository(private val apiInterface: ApiInterface) {

    suspend fun getAllRechargeAndBillServiceCharge(req: GetCommercialReq) = apiInterface.getAllRechargeAndBillServiceCharge(req)
    suspend fun getWalletBalance(req: GetBalanceReq) = apiInterface.getWalletBalance(req)
    suspend fun getAllMerchantBalance(req: GetMerchantBalanceReq) = apiInterface.getAllMerchantBalance(req)

    suspend fun getTransferAmountToAgents(req: TransferAmountToAgentsReq) = apiInterface.getTransferAmountToAgents(req)
    suspend fun getAllApiPayoutCommercialCharge(req: GetPayoutCommercialReq) = apiInterface.getAllApiPayoutCommercialCharge(req)
    suspend fun getAllMenuList(req: GetAllMenuListReq) = apiInterface.getAllMenuList(req)
    suspend fun getAllTransactionReport(req: TransactionReportReq) = apiInterface.getAllTransactionReport(req)

    // air requery request ..........................
    suspend fun getAirRequeryRequest(req: FlightRequeryReq) = apiInterface.getAirTicketRequeryReq(req)

    suspend fun getAirTicketListRequest(req: GetAirTicketListReq) = apiInterface.getAirTicketListReq(req)

    suspend fun getDashboardBanner(rid: Int,retailercode: String, task: String) = apiInterface.getdashboardbanner(rid,retailercode, task)

    suspend fun getRetailerWiseServicesRequest(req: RetailerWiseServicesRequest) = apiInterface.getRetailerWiseServices(req)

    suspend fun getBankDetails(req: CheckBankDetailsModel)= apiInterface.getBankDetails(req)

    suspend fun updateBankDetails(req: UpdateBankDetailsReq)= apiInterface.updateBankDetails(req)

    suspend fun getRetailerContactList(req: RetailerContactListRequestModel)= apiInterface.gettingAllRetailerContactList(req)

    suspend fun sendMoneyToMobileReqModel(req: SendMoneyToMobileReqModel)= apiInterface.sendToMobileMoney(req)


}