package com.bos.payment.appName.data.model.recharge.mobile

import com.google.gson.annotations.SerializedName


data class Info (

  @SerializedName("operator" ) var operator : String? = null,
  @SerializedName("circle"   ) var circle   : String? = null

)