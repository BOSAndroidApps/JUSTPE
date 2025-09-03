package com.bos.payment.appName.data.model

import java.io.Serializable

data class AddFundModel(
    val TransIpAddress: String, val API_TransId: String,
    val Amt_Transfer_TransID: String, val TransferToMsg: String,
    val TransferFromMsg: String, val Amount_Type: String,
    val Remark: String, val TransactionDate: String,
    val TransferFrom: String, val TransferTo: String,
    val TransferAmt: String, val RecordDateTime: String,
    val UpdatedBy: String, val UpdatedOn: String,
    val orderid: String, val companyCode: String
) : Serializable {
    constructor() : this(
        "", "", "", "",
        "", "", "", "",
        "", "", "", "",
        "", "", "", "",
    )
}

data class AddFundResultModel(val Status: String, val code: String, val Message: String) :
    Serializable {
    constructor(error: String) : this("False", "400", "Something Went Wrong!")
}
