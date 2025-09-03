package com.bos.payment.appName.data.model.reports.rechargeAndBill

import com.google.gson.annotations.SerializedName


data class RechargeWiseReportRes (

  @SerializedName("RID"                  ) var RID                 : Int?    = null,
  @SerializedName("Date"                 ) var Date                : String? = null,
  @SerializedName("Time"                 ) var Time                : String? = null,
  @SerializedName("CANo"                 ) var CANo                : String? = null,
  @SerializedName("Operator"             ) var Operator            : String? = null,
  @SerializedName("Amount"               ) var Amount              : String? = null,
  @SerializedName("TRANS_NO"             ) var TRANSNO             : String? = null,
  @SerializedName("Recharge_ServiceType" ) var RechargeServiceType : String? = null,
  @SerializedName("API_status"           ) var APIStatus           : String? = null,
  @SerializedName("API_resText"          ) var APIResText          : String? = null,
  @SerializedName("Status"               ) var Status              : Boolean? = null,
  @SerializedName("message"              ) var message             : String? = null,
  @SerializedName("Value"                ) var Value               : String? = null

)