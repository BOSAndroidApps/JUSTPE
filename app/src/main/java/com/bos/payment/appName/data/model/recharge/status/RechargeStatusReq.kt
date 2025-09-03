package com.bos.payment.appName.data.model.recharge.status

import com.google.gson.annotations.SerializedName


data class RechargeStatusReq (

  @SerializedName("registrationID" ) var registrationID : String? = null,
  @SerializedName("referenceid"    ) var referenceid    : String? = null

)