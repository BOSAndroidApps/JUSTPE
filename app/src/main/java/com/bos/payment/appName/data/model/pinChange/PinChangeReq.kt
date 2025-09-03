package com.bos.payment.appName.data.model.pinChange

import com.google.gson.annotations.SerializedName


data class PinChangeReq (

  @SerializedName("GroupId"        ) var GroupId        : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("TransactionPin" ) var TransactionPin : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)