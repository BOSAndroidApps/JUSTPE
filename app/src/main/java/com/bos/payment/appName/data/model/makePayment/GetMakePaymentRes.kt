package com.bos.payment.appName.data.model.makePayment

import com.google.gson.annotations.SerializedName


data class GetMakePaymentRes (

  @SerializedName("Status"  ) var Status  : Boolean? = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("Value"   ) var Value   : String? = null

)