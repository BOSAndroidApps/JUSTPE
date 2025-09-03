package com.bos.payment.appName.data.model.dmt.transaction

import com.google.gson.annotations.SerializedName


data class TransactStatusReq (

  @SerializedName("referenceid"    ) var referenceid    : String?    = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)