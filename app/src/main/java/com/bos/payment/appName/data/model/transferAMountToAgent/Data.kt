package com.bos.payment.appName.data.model.transferAMountToAgent

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("refTransID"  ) var refTransID  : String? = null,
  @SerializedName("requestData" ) var requestData : String? = null

)