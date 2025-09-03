package com.bos.payment.appName.data.model.dmt.queryRemitters

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("limit"  ) var limit  : String? = null,
  @SerializedName("mobile" ) var mobile : String? = null

)