package com.bos.payment.appName.data.model.pinChange

import com.google.gson.annotations.SerializedName


data class ChangePasswordRes (

  @SerializedName("AgentType"      ) var AgentType      : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null,
  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("AgentPassword"  ) var AgentPassword  : String? = null,
  @SerializedName("Status"         ) var Status         : String? = null,
  @SerializedName("message"        ) var message        : String? = null,
  @SerializedName("Value"          ) var Value          : String? = null

)