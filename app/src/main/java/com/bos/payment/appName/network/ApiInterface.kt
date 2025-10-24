package com.bos.payment.appName.network

import com.bos.payment.appName.data.model.AddFundModel
import com.bos.payment.appName.data.model.AddFundResultModel
import com.bos.payment.appName.data.model.AgentRefrenceid.AgentRefrenceidReq
import com.bos.payment.appName.data.model.AgentRefrenceid.AgentRefrenceidRes
import com.bos.payment.appName.data.model.recharge.status.RechargeStatusReq
import com.bos.payment.appName.data.model.recharge.recharge.MobileRechargeRes
import com.bos.payment.appName.data.model.creditCard.CreditCardBillPaymentReq
import com.bos.payment.appName.data.model.creditCard.CreditCardBillPaymentRes
import com.bos.payment.appName.data.model.dmt.bankList.DMTBankListReq
import com.bos.payment.appName.data.model.dmt.bankList.DMTBankListRes
import com.bos.payment.appName.data.model.dmt.fetchBenificary.FetchBeneficiaryReq
import com.bos.payment.appName.data.model.dmt.fetchBenificary.FetchBeneficiaryRes
import com.bos.payment.appName.data.model.dmt.queryRemitters.QueryRemitterReq
import com.bos.payment.appName.data.model.dmt.queryRemitters.QueryRemitterRes
import com.bos.payment.appName.data.model.dmt.registerBeneficiary.RegisterBaneficiaryReq
import com.bos.payment.appName.data.model.dmt.registerBeneficiary.RegisterBaneficiaryRes
import com.bos.payment.appName.data.model.dmt.registerRemitters.RegisterRemitterReq
import com.bos.payment.appName.data.model.dmt.registerRemitters.RegisterRemitterRes
import com.bos.payment.appName.data.model.dmt.transaction.TransactStatusReq
import com.bos.payment.appName.data.model.dmt.transaction.TransactStatusRes
import com.bos.payment.appName.data.model.dmt.transaction.TransactionReq
import com.bos.payment.appName.data.model.dmt.transaction.TransactionRes
import com.bos.payment.appName.data.model.dmt.transactionOtp.TransactionSendOtpReq
import com.bos.payment.appName.data.model.dmt.transactionOtp.TransactionSendOtpRes
import com.bos.payment.appName.data.model.fastTag.billPayment.BillPaymentPaybillReq
import com.bos.payment.appName.data.model.fastTag.billPayment.BillPaymentPaybillRes
import com.bos.payment.appName.data.model.fastTag.customerDetails.FetchConsumerDetailsReq
import com.bos.payment.appName.data.model.fastTag.fastTagOperator.FastTagOperatorsListRes
import com.bos.payment.appName.data.model.fastTag.recharge.FastTagRechargeReq
import com.bos.payment.appName.data.model.fastTag.recharge.FastTagRechargeRes
import com.bos.payment.appName.data.model.fastTag.viewBillPayment.FetchBilPaymentDetailsReq
import com.bos.payment.appName.data.model.fastTag.viewBillPayment.FetchBilPaymentDetailsRes
import com.bos.payment.appName.data.model.forgotPassWord.ForgotReq
import com.bos.payment.appName.data.model.forgotPassWord.ForgotRes
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutReq
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutRes
import com.bos.payment.appName.data.model.justpaymodel.BankDetailsResponseModel
import com.bos.payment.appName.data.model.justpaymodel.CheckBankDetailsModel
import com.bos.payment.appName.data.model.justpaymodel.GenerateQRCodeResponse
import com.bos.payment.appName.data.model.justpaymodel.GenerateVirtualAccountModel
import com.bos.payment.appName.data.model.justpaymodel.GenerateVirtualBankDetailsResponseModel
import com.bos.payment.appName.data.model.justpaymodel.UpdateBankDetailsReq
import com.bos.payment.appName.data.model.justpaymodel.UpdateBankDetailsResponse
import com.bos.payment.appName.data.model.justpedashboard.DashboardBannerListModel
import com.bos.payment.appName.data.model.justpedashboard.RetailerWiseServicesRequest
import com.bos.payment.appName.data.model.justpedashboard.RetailerWiseServicesResponse
import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCReq
import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCRes
import com.bos.payment.appName.data.model.kyc.UpdateKYCReq
import com.bos.payment.appName.data.model.kyc.UpdateKYCRes
import com.bos.payment.appName.data.model.loginSignUp.LoginRes
import com.bos.payment.appName.data.model.loginSignUp.signUp.SignUpReq
import com.bos.payment.appName.data.model.loginSignUp.signUp.SignUpRes
import com.bos.payment.appName.data.model.loginSignUp.ValidateReferenceIdRes
import com.bos.payment.appName.data.model.makePayment.GetMakePaymentReq
import com.bos.payment.appName.data.model.makePayment.GetMakePaymentRes
import com.bos.payment.appName.data.model.menuList.GetAllMenuListReq
import com.bos.payment.appName.data.model.menuList.GetAllMenuListRes
import com.bos.payment.appName.data.model.merchant.activeInActiveStatus.GetAPIActiveInactiveStatusReq
import com.bos.payment.appName.data.model.merchant.activeInActiveStatus.GetAPIActiveInactiveStatusRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.mobileCharge.GetCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.mobileCharge.GetCommercialRes
import com.bos.payment.appName.data.model.merchant.clientDetails.GetClientRegistrationReq
import com.bos.payment.appName.data.model.merchant.clientDetails.GetClientRegistrationRes
import com.bos.payment.appName.data.model.merchant.merchantList.GetApiListMarchentWiseReq
import com.bos.payment.appName.data.model.merchant.merchantList.GetApiListMarchentWiseRes
import com.bos.payment.appName.data.model.merchant.redirectUrl.RedirectUrlVerifyReq
import com.bos.payment.appName.data.model.merchant.redirectUrl.RedirectUrlVerifyRes
import com.bos.payment.appName.data.model.notification.GetNotificationReq
import com.bos.payment.appName.data.model.notification.GetNotificationRes
import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.data.model.otp.OtpSubmitRes
import com.bos.payment.appName.data.model.pinChange.ChangePasswordReq
import com.bos.payment.appName.data.model.pinChange.ChangePasswordRes
import com.bos.payment.appName.data.model.pinChange.PinChangeReq
import com.bos.payment.appName.data.model.pinChange.PinChangeRes
import com.bos.payment.appName.data.model.recharge.plan.MobileBrowserPlanReq
import com.bos.payment.appName.data.model.recharge.plan.MobileBrowserPlanRes
import com.bos.payment.appName.data.model.recharge.BillOperationPaymentReq
import com.bos.payment.appName.data.model.recharge.BillOperationPaymentRes
import com.bos.payment.appName.data.model.recharge.beneficiary.AddBeneficiaryReq
import com.bos.payment.appName.data.model.recharge.beneficiary.AddBeneficiaryRes
import com.bos.payment.appName.data.model.recharge.mobile.MobileCheckReq
import com.bos.payment.appName.data.model.recharge.mobile.MobileCheckRes
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileRechargeRespo
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileWiseRechargeReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileWiseRechargeResp
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeCategoryReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeCategoryResponse
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeOperatorNameResp
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeOperatorsReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargePlanReq
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.RechargeViewPlanResponse
import com.bos.payment.appName.data.model.recharge.operator.RechargeOperatorsListReq
import com.bos.payment.appName.data.model.recharge.operator.RechargeOperatorsListRes
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountReq
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountRes
import com.bos.payment.appName.data.model.recharge.payout.payoutStatus.PayoutStatusReq
import com.bos.payment.appName.data.model.recharge.qrCode.GenerateQRCodeReq
import com.bos.payment.appName.data.model.recharge.qrCode.GenerateQRCodeRes
import com.bos.payment.appName.data.model.recharge.recharge.DthInfoPlanResp
import com.bos.payment.appName.data.model.recharge.recharge.DthInfoReq
import com.bos.payment.appName.data.model.recharge.recharge.MobileRechargeReq
import com.bos.payment.appName.data.model.recharge.rechargeHistory.RechargeHistoryReq
import com.bos.payment.appName.data.model.recharge.rechargeHistory.RechargeHistoryRes
import com.bos.payment.appName.data.model.recharge.status.RechargeStatusRes
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportReq
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportRes
import com.bos.payment.appName.data.model.serviceWiseTrans.ServiceWiseTransactionReq
import com.bos.payment.appName.data.model.serviceWiseTrans.ServiceWiseTransactionRes
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportReq
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportRes
import com.bos.payment.appName.data.model.stateDistrict.GetStateRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsWithCalculationReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsWithCalculationRes
import com.bos.payment.appName.data.model.travel.bus.busTicket.AddTicketReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.AddTicketResp
import com.bos.payment.appName.data.model.travel.bus.busTicket.AddTicketResponseReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.AddTicketResponseRes
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusBookingListReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusBookingListRes
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusCancelTicketListRespo
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusPassangerDetailsRes
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusPassengerDetailsReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusPaxRequeryResponseReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusTampBookTicketResponseRequest
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusTampBookingResp
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusTempBookingRequest
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusTicketCancelRespoResponse
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusTicketCancelResponseReq
import com.bos.payment.appName.data.model.travel.flight.AirportListReq
import com.bos.payment.appName.data.model.travel.flight.AirportListResp
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryReq
import com.bos.payment.appName.data.model.travel.flight.FlightRequeryResponse
import com.bos.payment.appName.data.model.travel.flight.GetAirTicketListReq
import com.bos.payment.appName.data.model.travel.flight.airbookingticketList.AirTicketListResp
import com.bos.payment.appName.data.model.walletBalance.ReturnWalletBalanceReq
import com.bos.payment.appName.data.model.walletBalance.ReturnWalletBalanceRes
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.WalletBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.WalletBalanceRes
import com.example.example.FetchConsumerDetailsRes
import com.example.example.LoginReq
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

/*   BASE URL =  https://bosapi.bos.center*/

     @FormUrlEncoded
     @POST("api/Banner/BannerDetail")
      suspend fun getdashboardbanner(@Field("RID") rid:Int, @Field("RetailerCode") retailercode : String, @Field("Task") task : String):Response<DashboardBannerListModel>


    @POST("api/Feature/GetRetailerServices")
    suspend fun getRetailerWiseServices(@Body req: RetailerWiseServicesRequest): Response<RetailerWiseServicesResponse>?

    // check qrcode status......................................................

    @POST("api/Retailer/GetRetailerBankDetails")
    suspend fun getBankDetails(@Body req: CheckBankDetailsModel): Response<BankDetailsResponseModel>?

    // update  BankDetails
    @POST("api/Retailer/UpdateQRCodeStatus")
    suspend fun updateBankDetails(@Body req: UpdateBankDetailsReq): Response<UpdateBankDetailsResponse>?


    // Generate virtual account
    @POST("api/AOP/SNBOSCreateVPA")
    suspend fun createVirtualAccount(@Body req: GenerateVirtualAccountModel): Response<GenerateVirtualBankDetailsResponseModel>?

    // Generate staticQR Code
    @POST("api/AOP/BOSVPAStaticQR")
    suspend fun createQRCode(@Body req: com.bos.payment.appName.data.model.justpaymodel.GenerateQRCodeReq): Response<GenerateQRCodeResponse>?


    @POST("/api/Login/UserLogin")
    suspend fun login(@Body req: LoginReq): Response<LoginRes>?

    @POST("api/BOS/SendOTPForgetPassword")
    suspend fun forgotPassword(@Body req: ForgotReq): Response<ForgotRes>?

    @POST("api/BOS/SubmitOTP")
    suspend fun submitOTP(@Body req: OtpSubmitReq): Response<OtpSubmitRes>?

    @POST("api/BOS/ValidateRefranceID")
    suspend fun validateReferenceId(@Body req: OtpSubmitReq): Response<ValidateReferenceIdRes>?

    @POST("api/Signup/Signup")
    suspend fun signUp(@Body req: SignUpReq): Response<SignUpRes>?

    @POST("api/BOS/ChangeTransactionPin")
    suspend fun changePin(@Body req: PinChangeReq): Response<PinChangeRes>?

    @POST("api/BOS/ServiceWiseTransactionReport")
    suspend fun serviceWiseReports(@Body req: ServiceWiseTransactionReq): Response<List<ServiceWiseTransactionRes>>?

    @POST("api/Reports/TransactionReport")
    suspend fun getAllTransactionReport(@Body req: TransactionReportReq): Response<TransactionReportRes>?

    @POST("api/BOS/UpdateAgentKycDetails")
    suspend fun updateKYC(@Body req: UpdateKYCReq): Response<UpdateKYCRes>?

    @POST("api/BOS/ReteriveAgentKycDetails")
    suspend fun reterieveKYCDetails(@Body req: ReteriveAgentKYCReq): Response<List<ReteriveAgentKYCRes>>?

    @POST("api/BOS/ChangeOperatorPassword")
    suspend fun changePassword(@Body req: ChangePasswordReq): Response<ChangePasswordRes>?

    @POST("api/BOS/StateList")
    suspend fun getState(): Response<List<GetStateRes>?>?

    @POST("api/BOS/BOSRechargeWiseReport")
    suspend fun rechargeBill(@Body req: RechargeWiseReportReq): Response<List<RechargeWiseReportRes>?>?

    @POST("api/BOS/App/QueryRemitter")
//    fun getDMTDetailsByMobileNumber(@Body dmtModel: DMTModel?): Call<DMTModel?>?
    suspend fun getDMTDetailsByMobileNumber(@Body req: QueryRemitterReq?): Response<QueryRemitterRes>?

    @POST("api/BOS/App/FetchBeneficiary")
//    fun getDMTFetchBeneficiary(@Body receiptModel: RecieptDetailsModel?): Call<RecieptDetailsModel?>?
    suspend fun getDMTFetchBeneficiary(@Body req: FetchBeneficiaryReq?): Response<FetchBeneficiaryRes>?

    @POST("api/BOS/App/RegisterRemitter")
    suspend fun registerRemitters(@Body req: RegisterRemitterReq?): Response<RegisterRemitterRes?>?

    @POST("api/BOS/App/RegisterBaneficiary")
//    fun registerReceipt(@Body registerModel: RegisterReceiptModel?): Call<RegisterReceiptModel?>?
    suspend fun registerBeneficiary(@Body req: RegisterBaneficiaryReq?): Response<RegisterBaneficiaryRes?>?

    @POST("api/BOS/App/TransactionSendOtp")
    suspend fun getTransactionOtp(@Body req: TransactionSendOtpReq): Response<TransactionSendOtpRes>?

    @POST("api/BOS/App/Transaction")
    suspend fun transactionAmountFromDMT(@Body req: TransactionReq): Response<TransactionRes>?

    @POST("api/V5/Payout/BOSPayoutApp")
    suspend fun getPayoutAmountTransaction(@Body req: com.bos.payment.appName.data.model.recharge.payout.payoutBos.PayoutAppReq): Response<PayoutAmountRes>

    @POST("api/BOS/App/TransactStatus")
    suspend fun getAllTransactionStatus(@Body req: TransactStatusReq): Response<TransactStatusRes>?
/*
    @POST("api/BOS/App/RechargeOperatorsList")
    suspend fun getAllOperatorList(@Body req: RechargeOperatorsListReq): Response<RechargeOperatorsListRes>?*/

    @POST("api/AOP/App/RechargeOperatorsList")
    suspend fun getAllOperatorList(@Body req: RechargeOperatorsListReq): Response<RechargeOperatorsListRes>?

    //    @POST("api/BOS/BillPaymentOperatorsList")
    //    fun getOperatorList(@Body model: Operation?): Call<OperationListModel?>?

  /*  @POST("api/BOS/App/hlrcheck")
      suspend fun getOperatorName(@Body req: MobileCheckReq?): Response<MobileCheckRes>? */ // Annu

    @POST("api/AOP/App/hlrcheck")
    suspend fun getOperatorName(@Body req: MobileCheckReq?): Response<MobileCheckRes>?

    // Like master API //
    @POST("api/BOS/App/BillPaymentOperatorsList")
    suspend fun getOperatorList(@Body model: BillOperationPaymentReq?): Response<BillOperationPaymentRes>?

   /* @POST("api/BOS/App/BrowsPlan")
    suspend fun getAllPlanList(@Body model: MobileBrowserPlanReq): Response<MobileBrowserPlanRes?>?*/

     @POST("api/AOP/App/BrowsPlan")
    suspend fun getAllPlanList(@Body model: MobileBrowserPlanReq): Response<MobileBrowserPlanRes?>?


    @POST("api/BOS/App/DoRecharge")
    suspend fun doRecharge(@Body req: MobileRechargeReq?): Response<MobileRechargeRes>?


    @POST("api/BOS/App/FetchConsumerDetails")
    fun getFastTagDetails(@Body req: FetchConsumerDetailsReq?): Call<FetchConsumerDetailsRes?>?

    @POST("api/BOS/App/OperatorsList")
    suspend fun getFastTagList(@Query("RegistrationID") registrationID: String): Response<FastTagOperatorsListRes?>?

//    @POST("api/BOS/FetchBilPaymentDetails")
//    fun viewBill(@Body model: BillModel?): Call<BillModelGet?>?

    @POST("api/BOS/App/FetchBilPaymentDetails")
    suspend fun viewBill(@Body req: FetchBilPaymentDetailsReq?): Response<FetchBilPaymentDetailsRes>?

    //    fun getOperatorName(@Body receiptModel: OperationSendModel?): Call<OperationModel?>?
    //    @POST("hlrcheck")

    @POST("api/BOS/App/Recharge")
    fun billFastTagRecharge(@Body model: FastTagRechargeReq?): Call<FastTagRechargeRes?>?

//    @POST("BillPaymentPaybill")
//    fun billRecharge(@Body model: BillRechargeModel?): Call<BillDataResult?>?

    @POST("api/BOS/App/BillPaymentPaybill")
    fun billRecharge(@Body model: BillPaymentPaybillReq?): Call<BillPaymentPaybillRes?>?

    @POST("api/BOS/App/CreditCardBillPayment")
    suspend fun creditCardDetails(@Body model: CreditCardBillPaymentReq?): Response<CreditCardBillPaymentRes>?

    @POST("api/BOS/AddFund")
    fun addFund(@Body model: AddFundModel?): Call<AddFundResultModel?>?

    @POST("api/BOS/App/RechargesStatus")
    suspend fun rechargeStatus(@Body receiptModel: RechargeStatusReq?): Response<RechargeStatusRes>?

//    @POST("api/BOS/RechargesHistory")
    @POST("/api/Recharge/RechargeReport")
    suspend fun transactionHistoryList(@Body req: RechargeHistoryReq): Response<RechargeHistoryRes>?

//    @POST("api/BOS/App/V4/StaticQRCode")
    // new Bos generate QR code
    @POST("api/BOS/BOSVPAStaticQR")
    suspend fun generateQRCode(@Body req: GenerateQRCodeReq): Response<GenerateQRCodeRes>?

    @POST("api/BOS/FPAddBeneficiary")
    suspend fun getBeneficiary(@Body req: AddBeneficiaryReq?): Response<AddBeneficiaryRes>?

//    @POST("api/V5/Payout/BOSUPIPayout")
    @POST("api/V5/Payout/BOSUPIPayoutApp")
    suspend fun sendAmount(@Body req: PayoutAmountReq?): Response<PayoutAmountRes>?

    @POST("api/V5/Payout/BOSPayoutStatus")
    suspend fun getPayoutStatus(@Body req: PayoutStatusReq): Response<PayoutAmountRes>?

    @POST("api/BOS/GetNotification")
    suspend fun getNotification(@Body req: GetNotificationReq): Response<List<GetNotificationRes>?>?


    @POST("api/BOS/RedirectUrlVerify")
    suspend fun getReDirectUrl(@Body req: RedirectUrlVerifyReq?): Response<RedirectUrlVerifyRes>?


    @POST("api/MerchantApi/ActiveServices")
    suspend fun getAllMerchantList(@Body req: GetApiListMarchentWiseReq): Response<GetApiListMarchentWiseRes>?


    @POST("api/BOS/GetClientRegistration")
    suspend fun getClientDetails(@Body req: GetClientRegistrationReq): Response<List<GetClientRegistrationRes>?>?


    @POST("api/BOS/GetMakePayment")
    suspend fun getAllMakePayment(@Body req: GetMakePaymentReq): Response<GetMakePaymentRes>?


    @POST("api/BOS/App/DMTBankList")
    suspend fun getAllDMTBankList(@Body req: DMTBankListReq): Response<List<DMTBankListRes>?>?


    @POST("api/BOS/GetAPIActiveInactiveStatus")
    suspend fun getAllAPIRetailerWiseActiveInActive(@Body req: GetAPIActiveInactiveStatusReq): Response<GetAPIActiveInactiveStatusRes>?


    @POST("api/BOS/GetAPIServiceCharge")
    suspend fun getAllAPIServiceCharge(@Body req: GetAPIServiceChargeReq): Response<List<GetAPIServiceChargeRes>>?


    //only commercial for payout
    @POST("api/Commercial/GetAndroidCommercial")
    suspend fun getAllApiPayoutCommercialCharge(@Body req: GetPayoutCommercialReq): Response<GetPayoutCommercialRes>?


    @POST("api/Commercial/GeTCommercial")
    suspend fun getAllRechargeAndBillServiceCharge(@Body req: GetCommercialReq): Response<GetCommercialRes>?


    @POST("api/BOS/ReturnWalletBalance")
    suspend fun getWalletBalance(@Body req: ReturnWalletBalanceReq): Response<ReturnWalletBalanceRes>?


    @POST("api/BOS/GetWalletBalance")
    suspend fun getAllWalletBalance(@Body req: WalletBalanceReq): Response<WalletBalanceRes>?


    @POST("api/Balance/MerchantDebitBalance")
    suspend fun getAllMerchantBalance (@Body req: GetMerchantBalanceReq): Response<GetMerchantBalanceRes>?


    @POST("api/Balance/GetBalance")
    suspend fun getWalletBalance(@Body req: GetBalanceReq): Response<GetBalanceRes>?


    @POST("/api/Payout/BosPayoutTransaction")
    suspend fun getTransferAmountToAgents(@Body req: TransferAmountToAgentsReq): Response<TransferAmountToAgentsRes>?


    @POST("api/BOS/AgentRefrenceid")
    suspend fun getAgentReferenceId(@Body req: AgentRefrenceidReq): Response<AgentRefrenceidRes>?


    @POST("api/MenuList/GetMenu")
    suspend fun getAllMenuList(@Body req: GetAllMenuListReq): Response<GetAllMenuListRes>?


    @POST("api/BOS/TransferAmountToAgentsWith_Calculation")
    suspend fun getTransferAmountToAgentWithCal(@Body req: TransferAmountToAgentsWithCalculationReq): Response<TransferAmountToAgentsWithCalculationRes>?


    @POST("api/V6/Payout/AOPPayout")
    suspend fun sendAllPayoutAmount(@Body req: AOPPayOutReq): Response<AOPPayOutRes>?


    @POST("api/BusApi/BusAddTicketRequest")
    suspend fun getAddTicketRequest(@Body req: AddTicketReq): Response<AddTicketResp>? // Annu


    @POST("api/BusApi/tickets/response")
    suspend fun getAddTicketResponse(@Body req: AddTicketResponseReq): Response<AddTicketResponseRes>? // Annu


    @POST("api/BusApi/bookingList")
    suspend fun getAllBusBookingList(@Body req: BusBookingListReq): Response<BusBookingListRes>? // Annu


    @POST("api/BusApi/BusCancelResponse")
    suspend fun getAllBusTicketCancelResponseReq(@Body req: BusTicketCancelResponseReq): Response<BusTicketCancelRespoResponse>? // Annu

    @POST("api/BusApi/BusTempBookingRequest")
    suspend fun getBusTempBookingRequest(@Body req: BusTempBookingRequest): Response<BusTampBookingResp>? // Annu


    @POST("api/BusApi/BusTempBookingResponse")
    suspend fun getBusTempBookingRequest(@Body req: BusTampBookTicketResponseRequest): Response<BusTampBookingResp>? // Annu



    @POST("api/BusApi/requerylist")
    suspend fun getPassangerDetails(@Body req: BusPassengerDetailsReq): Response<BusPassangerDetailsRes>?


    @POST("api/BusApi/BusReueryResponse")
    suspend fun getPaxRequeryResponseReq(@Body req: BusPaxRequeryResponseReq): Response<BusBookingListRes>? //Annu this api using at ticket book and cancel both time for update data in db


    @POST("api/BusApi/BusCancelList")
    suspend fun getBusCancelList(@Body req: BusBookingListReq): Response<BusCancelTicketListRespo>?


    @POST("api/Airport/Airportlist")
    suspend fun getAirPortList(@Body req: AirportListReq): Response<AirportListResp>?


    @POST("api/Air/api/AirReueryResponse")
    suspend fun getAirTicketRequeryReq(@Body req: FlightRequeryReq): Response<FlightRequeryResponse>? // Annu


    @POST("api/Air/api/AirbookingList")
    suspend fun getAirTicketListReq(@Body req: GetAirTicketListReq): Response<AirTicketListResp>? // Annu

    // new api for recharge............................................................................

    @POST("api/AOP/V2/GetRechargeCategories")
    suspend fun getRechargeCategory(@Body req: RechargeCategoryReq): Response<RechargeCategoryResponse>? // Annu


    @POST("api/AOP/V2/GetRechargeSubCategories")
    suspend fun getRechargeOperatorNameReq(@Body req: RechargeOperatorsReq): Response<RechargeOperatorNameResp>? // Annu


    @POST("api/AOP/V2/RechargehlrCheck")
    suspend fun getMobileWiseRechargeReq(@Body req: MobileWiseRechargeReq): Response<MobileWiseRechargeResp>? // Annu


    @POST("api/AOP/V2/GetRechargeBrowsePlan")
    fun getRechargePlanReq(@Body body: RequestBody): Call<ResponseBody>


    @POST("api/AOP/V2/MobileRecharge")
    suspend fun getMobileRechargeReq(@Body req: com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.MobileRechargeReq): Response<MobileRechargeRespo>? // Annu


    @POST("api/AOP/V2/GetDthInfo")
    suspend fun getDthInfoPlanReq(@Body req: DthInfoReq): Response<DthInfoPlanResp>?





}