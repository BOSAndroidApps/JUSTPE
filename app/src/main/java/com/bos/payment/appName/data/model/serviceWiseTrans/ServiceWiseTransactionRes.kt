package com.bos.payment.appName.data.model.serviceWiseTrans

import com.google.gson.annotations.SerializedName


data class ServiceWiseTransactionRes(

    @SerializedName("SrNo") var SrNo: Int? = null,
    @SerializedName("TRANSACTIONNO") var TRANSACTIONNO: String? = null,
    @SerializedName("UPIRefID") var UPIRefID: String? = null,
    @SerializedName("DATE") var DATE: String? = null,
    @SerializedName("TIME") var TIME: String? = null,
    @SerializedName("TRANSFERFROM") var TRANSFERFROM: String? = null,
    @SerializedName("TRANSFERTO") var TRANSFERTO: String? = null,
    @SerializedName("SERVICETYPE") var SERVICETYPE: String? = null,
    @SerializedName("CR") var CR: String? = null,
    @SerializedName("DR") var DR: String? = null,
    @SerializedName("BALANCE") var BALANCE: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("MobileNo") var MobileNo: String? = null,
    @SerializedName("VPAAccountID") var VPAAccountID: String? = null,
    @SerializedName("REMARKS") var REMARKS: String? = null,
    @SerializedName("Status") var Status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("Value") var Value: String? = null

)