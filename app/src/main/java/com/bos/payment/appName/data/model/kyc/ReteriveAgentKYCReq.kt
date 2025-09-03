package com.bos.payment.appName.data.model.kyc

import com.google.gson.annotations.SerializedName


data class ReteriveAgentKYCReq (

  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode : String? = null

)