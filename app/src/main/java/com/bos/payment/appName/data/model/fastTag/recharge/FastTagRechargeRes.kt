package com.bos.payment.appName.data.model.fastTag.recharge

import com.google.gson.annotations.SerializedName


data class FastTagRechargeRes (

  @SerializedName("response_code" ) var responseCode : Int?     = null,
  @SerializedName("status"        ) var status       : Boolean? = null,
  @SerializedName("message"       ) var message      : String?  = null

)