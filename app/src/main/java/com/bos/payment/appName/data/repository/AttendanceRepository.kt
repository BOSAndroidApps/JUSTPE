package com.bos.payment.appName.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bos.payment.appName.constant.ConstantClass
import com.bos.payment.appName.data.model.AddFundModel
import com.bos.payment.appName.data.model.AddFundResultModel
import com.bos.payment.appName.data.model.fastTag.billPayment.BillPaymentPaybillReq
import com.bos.payment.appName.data.model.fastTag.billPayment.BillPaymentPaybillRes
import com.bos.payment.appName.data.model.fastTag.customerDetails.FetchConsumerDetailsReq
import com.bos.payment.appName.data.model.fastTag.recharge.FastTagRechargeReq
import com.bos.payment.appName.data.model.fastTag.recharge.FastTagRechargeRes
import com.bos.payment.appName.network.RetrofitClient
import com.example.example.FetchConsumerDetailsRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceRepository {
    val userDataService = RetrofitClient.getService()
//  val apiInterface = RetrofitClient.apiInterface
    val apiAllInterface = RetrofitClient.apiAllInterface

//    fun getDMTDetailsByMobileNumber(StrMobileNumber: String?, StrAppType: String?): LiveData<DMTModel?> {
//        val mutableLiveData = MutableLiveData<DMTModel?>()
//        val call = apiAllInterface.getDMTDetailsByMobileNumber(DMTModel(StrMobileNumber, StrAppType))
//        call!!.enqueue(object : Callback<DMTModel?> {
//            override fun onResponse(call: Call<DMTModel?>, resp: Response<DMTModel?>) {
//                if (resp.body() != null && resp.isSuccessful && resp.body()!!.status) {
//                    mutableLiveData.setValue(resp.body())
//                } else if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(DMTModel(resp.body()!!.message,resp.body()!!.message))
//                } else {
//                    mutableLiveData.setValue(DMTModel(ConstantClass.api_response_default_Message,ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<DMTModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = DMTModel(t.localizedMessage,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }

//    fun login(StrUserName: String?, StrPassword: String?, StrCompanycode:String?): LiveData<LoginModel?> {
//        val mutableLiveData = MutableLiveData<LoginModel?>()
//        val call = userDataService!!.login(StrUserName, StrPassword,StrCompanycode)
//        call!!.enqueue(object : Callback<LoginModel?> {
//            override fun onResponse(call: Call<LoginModel?>, resp: Response<LoginModel?>) {
//                if (resp.body() != null && resp.isSuccessful && resp.body()!!.Status == ConstantClass.Success) {
//                    mutableLiveData.value =resp.body()
//                }else if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.value = LoginModel(resp.body()!!.Status)
//                }  else {
//                    mutableLiveData.value = LoginModel(ConstantClass.api_response_default_Message)
//                }
//            }
//            override fun onFailure(call: Cal
//            l<LoginModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = LoginModel(t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun register(registerModel: RegisterModel): LiveData<RegisterModel?> {
//        val mutableLiveData = MutableLiveData<RegisterModel?>()
//        val call = apiAllInterface.registerRemitters(registerModel)
//
//        call!!.enqueue(object : Callback<RegisterModel?> {
//            override fun onResponse(call: Call<RegisterModel?>, resp: Response<RegisterModel?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(RegisterModel(ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<RegisterModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = RegisterModel(t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun registerReceipt(registerModel: RegisterReceiptModel): LiveData<RegisterReceiptModel?> {
//        val mutableLiveData = MutableLiveData<RegisterReceiptModel?>()
//        val call = userDataService!!.registerBeneficiary(registerModel)
//
//        call!!.enqueue(object : Callback<RegisterReceiptModel?> {
//            override fun onResponse(call: Call<RegisterReceiptModel?>, resp: Response<RegisterReceiptModel?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(RegisterReceiptModel(ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<RegisterReceiptModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = RegisterReceiptModel(t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun getDMTReceiptDetails(StrMobileNumber: String): LiveData<RecieptDetailsModel?> {
//        val mutableLiveData = MutableLiveData<RecieptDetailsModel?>()
//        val call = userDataService!!.getDMTFetchBeneficiary(RecieptDetailsModel(StrMobileNumber))
//        call!!.enqueue(object : Callback<RecieptDetailsModel?> {
//            override fun onResponse(call: Call<RecieptDetailsModel?>, resp: Response<RecieptDetailsModel?>) {
//                if (resp.body() != null && resp.isSuccessful && resp.body()!!.status) {
//                    mutableLiveData.setValue(resp.body())
//                } else if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(RecieptDetailsModel(resp.body()!!.message))
//                } else {
//                    mutableLiveData.setValue(RecieptDetailsModel(ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<RecieptDetailsModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = RecieptDetailsModel(t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }

//    fun getOperatorList(): LiveData<OperationListModel?> {
//        val mutableLiveData = MutableLiveData<OperationListModel?>()
//        var model= Operation("")
//        val call = apiInterface.getOperatorList(model)
//        call!!.enqueue(object : Callback<OperationListModel?> {
//            override fun onResponse(call: Call<OperationListModel?>, resp: Response<OperationListModel?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(OperationListModel(false,ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<OperationListModel?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = OperationListModel(false,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun getOperatorList(): LiveData<BillOperationPaymentRes?> {
//        val mutableLiveData = MutableLiveData<BillOperationPaymentRes?>()
//        var model= BillOperationPaymentReq("Online",RegistrationID = Constants.MerchantId)
//        val call = apiAllInterface.getOperatorList(model)
//        call!!.enqueue(object : Callback<BillOperationPaymentRes?> {
//            override fun onResponse(call: Call<BillOperationPaymentRes?>, resp: Response<BillOperationPaymentRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(BillOperationPaymentRes(200,ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<BillOperationPaymentRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = BillOperationPaymentRes(201,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }

//    fun getAllOperatorList(): LiveData<RechargeOperatorsListRes?> {
//        val mutableLiveData = MutableLiveData<RechargeOperatorsListRes?>()
//        var model= RechargeOperatorsListReq(registrationID = Constants.MerchantId,"Online")
//        val call = apiAllInterface.getAllOperatorList(model)
//        call!!.enqueue(object : Callback<RechargeOperatorsListRes?> {
//            override fun onResponse(call: Call<RechargeOperatorsListRes?>, resp: Response<RechargeOperatorsListRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(RechargeOperatorsListRes(200,ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<RechargeOperatorsListRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = RechargeOperatorsListRes(201,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }

//    fun getFastTagList(registrationID: String): LiveData<FastTagOperatorsListRes?> {
//        val mutableLiveData = MutableLiveData<FastTagOperatorsListRes?>()
//        val call = apiAllInterface.getFastTagList(registrationID)
//        call!!.enqueue(object : Callback<FastTagOperatorsListRes?> {
//            override fun onResponse(call: Call<FastTagOperatorsListRes?>, resp: Response<FastTagOperatorsListRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(FastTagOperatorsListRes(0,false))
//                }
//            }
//            override fun onFailure(call: Call<FastTagOperatorsListRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = FastTagOperatorsListRes(0,false)
//            }
//        })
//        return mutableLiveData
//    }
//    fun getOperatorName(model: MobileCheckReq): LiveData<MobileCheckRes?> {
//        val mutableLiveData = MutableLiveData<MobileCheckRes?>()
//        val call = apiAllInterface!!.getOperatorName(model)
//
//        call!!.enqueue(object : Callback<MobileCheckRes?> {
//            override fun onResponse(call: Call<MobileCheckRes?>, resp: Response<MobileCheckRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(
//                        MobileCheckRes(0,"false",
//                            Info(),ConstantClass.api_response_default_Message)
//                    )
//                }
//            }
//            override fun onFailure(call: Call<MobileCheckRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = MobileCheckRes(201,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }


//    fun getAllPlanList(model: MobileBrowserPlanReq): LiveData<MobileBrowserPlanRes?> {
//        val mutableLiveData = MutableLiveData<MobileBrowserPlanRes?>()
//        val call = apiAllInterface!!.getAllPlanList(model)
//
//        call!!.enqueue(object : Callback<MobileBrowserPlanRes?> {
//            override fun onResponse(call: Call<MobileBrowserPlanRes?>, resp: Response<MobileBrowserPlanRes?>) {
//                if (resp.body() != null && resp.body()!!.status == true) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(MobileBrowserPlanRes(false,ConstantClass.api_response_default_Message))
//                }
//            }
//            override fun onFailure(call: Call<MobileBrowserPlanRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = MobileBrowserPlanRes(false,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    @SuppressLint("SuspiciousIndentation")
//    fun doRecharge(model: MobileRechargeReq): LiveData<MobileRechargeRes?> {
//        val mutableLiveData = MutableLiveData<MobileRechargeRes?>()
//        val call = apiAllInterface.doRecharge(model)
//
//            call!!.enqueue(object : Callback<MobileRechargeRes?> {
//            override fun onResponse(call: Call<MobileRechargeRes?>, resp: Response<MobileRechargeRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(MobileRechargeRes(resp.body()!!.responsecode, false,
//                        resp.body()!!.message))
//                }
//            }
//            override fun onFailure(call: Call<MobileRechargeRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = MobileRechargeRes(201,false,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun rechargeStatus(model: RechargeStatusReq): LiveData<MobileRechargeRes?> {
//        val mutableLiveData = MutableLiveData<MobileRechargeRes?>()
//        val call = apiAllInterface.rechargeStatus(model)
//        call!!.enqueue(object : Callback<MobileRechargeRes?> {
//            override fun onResponse(call: Call<MobileRechargeRes?>, resp: Response<MobileRechargeRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(MobileRechargeRes(201))
//                }
//            }
//            override fun onFailure(call: Call<MobileRechargeRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = MobileRechargeRes(201,false,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun transactionHistoryList(model: RechargeHistoryReq): LiveData<List<RechargeHistoryRes?>> {
//        val mutableLiveData = MutableLiveData<List<RechargeHistoryRes?>>()
//        val call = apiAllInterface.transactionHistoryList(model)
//        call!!.enqueue(object : Callback<List<RechargeHistoryRes?>> {
//            override fun onResponse(call: Call<List<RechargeHistoryRes?>>, resp: Response<List<RechargeHistoryRes?>>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }  else {
//                    mutableLiveData.setValue(ArrayList())
//                }
//            }
//            override fun onFailure(call: Call<List<RechargeHistoryRes?>>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.setValue(ArrayList())
//              //  mutableLiveData.value = TransactionHistoryModelList(false,t.localizedMessage)
//            }
//        })
//        return mutableLiveData
//    }
//    fun viewBill(model: FetchBilPaymentDetailsReq): LiveData<FetchBilPaymentDetailsRes?> {
//        val mutableLiveData = MutableLiveData<FetchBilPaymentDetailsRes?>()
//        val call = apiAllInterface!!.viewBill(model)
//        call!!.enqueue(object : Callback<FetchBilPaymentDetailsRes?> {
//            override fun onResponse(call: Call<FetchBilPaymentDetailsRes?>, resp: Response<FetchBilPaymentDetailsRes?>) {
//                if (resp.body() != null && resp.isSuccessful) {
//                    mutableLiveData.setValue(resp.body())
//                }else  if (resp.body() != null && resp.body()!!.status == true) {
//                    mutableLiveData.setValue(FetchBilPaymentDetailsRes(0,false))
//                }   else {
//                    mutableLiveData.setValue(FetchBilPaymentDetailsRes(0,false))
//                }
//            }
//            override fun onFailure(call: Call<FetchBilPaymentDetailsRes?>, t: Throwable) {
//                t.printStackTrace()
//                mutableLiveData.value = FetchBilPaymentDetailsRes(0,false)
//            }
//        })
//        return mutableLiveData
//    }
    fun getFastTagDetails(model: FetchConsumerDetailsReq): LiveData<FetchConsumerDetailsRes?> {
        val mutableLiveData = MutableLiveData<FetchConsumerDetailsRes?>()
        val call = apiAllInterface.getFastTagDetails(model)
        call!!.enqueue(object : Callback<FetchConsumerDetailsRes?> {
            override fun onResponse(call: Call<FetchConsumerDetailsRes?>, resp: Response<FetchConsumerDetailsRes?>) {
                if (resp.body() != null && resp.isSuccessful) {
                    mutableLiveData.setValue(resp.body())
                }else  if (resp.body() != null && resp.body()!!.status == true) {
                    mutableLiveData.setValue(FetchConsumerDetailsRes(0,false))
                }   else {
                    mutableLiveData.setValue(FetchConsumerDetailsRes(0,false))
                }
            }
            override fun onFailure(call: Call<FetchConsumerDetailsRes?>, t: Throwable) {
                t.printStackTrace()
                mutableLiveData.value = FetchConsumerDetailsRes(0,false)
            }
        })
        return mutableLiveData
    }
    fun billFastTagRecharge(model: FastTagRechargeReq): LiveData<FastTagRechargeRes?> {
        val mutableLiveData = MutableLiveData<FastTagRechargeRes?>()
        val call = apiAllInterface.billFastTagRecharge(model)

        call!!.enqueue(object : Callback<FastTagRechargeRes?> {
            override fun onResponse(call: Call<FastTagRechargeRes?>, resp: Response<FastTagRechargeRes?>) {
                if (resp.body() != null && resp.isSuccessful) {
                    mutableLiveData.setValue(resp.body())
                }  else {
                    mutableLiveData.setValue(FastTagRechargeRes(0,false,ConstantClass.api_response_default_Message))
                }
            }
            override fun onFailure(call: Call<FastTagRechargeRes?>, t: Throwable) {
                t.printStackTrace()
                mutableLiveData.value = FastTagRechargeRes(0,false,t.localizedMessage)
            }
        })
        return mutableLiveData
    }
    fun billRecharge(model: BillPaymentPaybillReq): LiveData<BillPaymentPaybillRes?> {
        val mutableLiveData = MutableLiveData<BillPaymentPaybillRes?>()
        val call = apiAllInterface.billRecharge(model)

        call!!.enqueue(object : Callback<BillPaymentPaybillRes?> {
            override fun onResponse(call: Call<BillPaymentPaybillRes?>, resp: Response<BillPaymentPaybillRes?>) {
                if (resp.body() != null && resp.isSuccessful) {
                    mutableLiveData.setValue(resp.body())
                }  else {
                    mutableLiveData.setValue(BillPaymentPaybillRes(false,201,"",1,"","",ConstantClass.api_response_default_Message))
                }
            }
            override fun onFailure(call: Call<BillPaymentPaybillRes?>, t: Throwable) {
                t.printStackTrace()
                mutableLiveData.value = BillPaymentPaybillRes(false,201,"",1,"","",t.localizedMessage)
            }
        })
        return mutableLiveData
    }
    fun addFund(model: AddFundModel): LiveData<AddFundResultModel?> {
        val mutableLiveData = MutableLiveData<AddFundResultModel?>()
        val call = apiAllInterface.addFund(model)

        call!!.enqueue(object : Callback<AddFundResultModel?> {
            override fun onResponse(call: Call<AddFundResultModel?>, resp: Response<AddFundResultModel?>) {
                if (resp.body() != null && resp.isSuccessful) {
                    mutableLiveData.setValue(resp.body())
                }  else {
                    mutableLiveData.setValue(AddFundResultModel(ConstantClass.api_response_default_Message))
                }
            }
            override fun onFailure(call: Call<AddFundResultModel?>, t: Throwable) {
                t.printStackTrace()
                mutableLiveData.value = AddFundResultModel(t.localizedMessage)
            }
        })
        return mutableLiveData
    }
}