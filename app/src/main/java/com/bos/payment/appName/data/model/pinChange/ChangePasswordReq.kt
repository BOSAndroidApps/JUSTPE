package com.bos.payment.appName.data.model.pinChange

import com.google.gson.annotations.SerializedName


data class ChangePasswordReq (

  @SerializedName("GroupId"        ) var GroupId        : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("AgentPassword"  ) var AgentPassword  : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)