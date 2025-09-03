package com.bos.payment.appName.data.model.AgentRefrenceid

import com.google.gson.annotations.SerializedName


data class AgentRefrenceidReq (

  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)