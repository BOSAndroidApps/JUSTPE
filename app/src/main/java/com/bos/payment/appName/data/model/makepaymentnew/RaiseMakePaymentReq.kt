package com.bos.payment.appName.data.model.makepaymentnew

import java.io.File

data class RaiseMakePaymentReq(
    val Mode: String,
    val RID: String,
    val RefrenceID: String,
    val PaymentMode: String,
    val PaymentDate: String,
    val DepositBankName: String,
    val BranchCode_ChecqueNo: String,
    val Remarks: String,
    val TransactionID: String,
    val DocumentPath: String,
    val RecordDateTime: String,
    val UpdatedBy: String,
    val UpdatedOn: String,
    val ApprovedBy: String,
    val ApprovedDateTime: String,
    val ApporvedStatus: String,
    val RegistrationId: String,
    val ApporveRemakrs: String,
    val Amount: String,
    val CompanyCode: String,
    val BeneId: String,
    val AccountHolder: String,
    val PaymentType: String,
    val Flag: String,
    val AdminCode: String,
    val imagefile1: File ?
)









