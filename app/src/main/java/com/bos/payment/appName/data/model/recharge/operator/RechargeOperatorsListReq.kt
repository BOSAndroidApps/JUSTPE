package com.bos.payment.appName.data.model.recharge.operator

import com.google.gson.annotations.SerializedName


data class RechargeOperatorsListReq (

  @SerializedName("registrationID" ) var registrationID : String? = null,
  @SerializedName("RechargeType"   ) var RechargeType   : String? = null

)