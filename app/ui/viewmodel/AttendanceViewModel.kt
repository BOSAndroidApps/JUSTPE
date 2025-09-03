package com.bos.payment.appName.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bos.payment.appName.data.model.AddFundModel
import com.bos.payment.appName.data.model.fastTag.billPayment.BillPaymentPaybillReq
import com.bos.payment.appName.data.model.fastTag.customerDetails.FetchConsumerDetailsReq
import com.bos.payment.appName.data.model.fastTag.recharge.FastTagRechargeReq
import com.bos.payment.appName.data.repository.AttendanceRepository

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {
    var attendanceRepository: AttendanceRepository? = AttendanceRepository()

    //    fun register(registerModel: RegisterModel) = attendanceRepository!!.register(registerModel)
//    fun registerReciept(registerModel: RegisterReceiptModel) = attendanceRepository!!.registerReceipt(registerModel)
//    fun getOperatorList() = attendanceRepository!!.getOperatorList()
//    fun getAllOperatorList() = attendanceRepository!!.getAllOperatorList()
//    fun getFastTagList(registrationID: String?) = registrationID?.let {
//        attendanceRepository!!.getFastTagList(
//            it
//        )
//    }
//    fun getOperatorName(model: MobileCheckReq) = attendanceRepository!!.getOperatorName(model)


//    fun getAllPlanList(model: MobileBrowserPlanReq) = attendanceRepository!!.getAllPlanList(model)

    //    fun doRecharge(model: MobileRechargeReq) = attendanceRepository!!.doRecharge(model)
    fun billFastTagRecharge(model: FastTagRechargeReq) =
        attendanceRepository!!.billFastTagRecharge(model)

    fun billRecharge(model: BillPaymentPaybillReq) = attendanceRepository!!.billRecharge(model)
    fun addFund(model: AddFundModel) = attendanceRepository!!.addFund(model)
//    fun viewBill(model: FetchBilPaymentDetailsReq) = attendanceRepository!!.viewBill(model)
    fun getFastTagDetails(model: FetchConsumerDetailsReq) =
        attendanceRepository!!.getFastTagDetails(model)

    //    fun rechargeStatus(model: RechargeStatusReq) = attendanceRepository!!.rechargeStatus(model)
//    fun transactionHistoryList(model: RechargeHistoryReq) = attendanceRepository!!.transactionHistoryList(model)
//    fun getDMTDetailsByMobileNumber(StrMobileNumber: String?, StrAppType: String?) = attendanceRepository!!.getDMTDetailsByMobileNumber(StrMobileNumber, StrAppType)
//    fun getDMTReceiptDetails(StrMobileNumber: String) = attendanceRepository!!.getDMTReceiptDetails(StrMobileNumber)
//    fun login(StrLoginID: String?, StrUserPassword: String?, StrCompanycode: String?) =
//        attendanceRepository!!.login(StrLoginID, StrUserPassword, StrCompanycode)

}

