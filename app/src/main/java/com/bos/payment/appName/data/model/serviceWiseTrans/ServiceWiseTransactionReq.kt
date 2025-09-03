package com.bos.payment.appName.data.model.serviceWiseTrans

import com.google.gson.annotations.SerializedName


data class ServiceWiseTransactionReq (

  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("ReportCategory" ) var ReportCategory : String? = null,
  @SerializedName("CompanyCode" ) var CompanyCode : String? = null

)