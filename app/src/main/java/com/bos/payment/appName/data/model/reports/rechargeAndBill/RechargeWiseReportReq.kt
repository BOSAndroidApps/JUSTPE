package com.bos.payment.appName.data.model.reports.rechargeAndBill

import com.google.gson.annotations.SerializedName


data class RechargeWiseReportReq (

  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("ReportCategory" ) var ReportCategory : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)