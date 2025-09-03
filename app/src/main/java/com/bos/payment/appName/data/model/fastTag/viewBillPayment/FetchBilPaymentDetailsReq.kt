package com.bos.payment.appName.data.model.fastTag.viewBillPayment

import com.google.gson.annotations.SerializedName


data class FetchBilPaymentDetailsReq (

  @SerializedName("operator"       ) var operator       : String? = null,
  @SerializedName("canumber"       ) var canumber       : String? = null,
  @SerializedName("mode"           ) var mode           : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)