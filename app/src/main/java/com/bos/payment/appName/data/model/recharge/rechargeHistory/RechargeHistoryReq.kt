package com.bos.payment.appName.data.model.recharge.rechargeHistory

import com.google.gson.annotations.SerializedName


data class RechargeHistoryReq (
  @SerializedName("registrationID" ) var registrationID : String? = null,
  @SerializedName("reportCategory" ) var reportCategory : String? = null

)