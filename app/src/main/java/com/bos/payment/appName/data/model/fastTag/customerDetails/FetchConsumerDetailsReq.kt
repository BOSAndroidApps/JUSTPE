package com.bos.payment.appName.data.model.fastTag.customerDetails

import com.google.gson.annotations.SerializedName


data class FetchConsumerDetailsReq (

  @SerializedName("operator"       ) var operator       : String? = null,
  @SerializedName("canumber"       ) var canumber       : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)