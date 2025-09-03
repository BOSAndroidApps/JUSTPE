package com.bos.payment.appName.data.model.notification

import com.google.gson.annotations.SerializedName


data class GetNotificationReq (

  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null,
  @SerializedName("AgentType"      ) var AgentType      : String? = null

)