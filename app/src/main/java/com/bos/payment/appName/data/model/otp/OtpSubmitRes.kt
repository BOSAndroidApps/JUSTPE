package com.bos.payment.appName.data.model.otp

import com.google.gson.annotations.SerializedName


data class OtpSubmitRes (

  @SerializedName("AgentType"      ) var AgentType      : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null,
  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("EmailID"        ) var EmailID        : String? = null,
  @SerializedName("AgentPassword"  ) var AgentPassword  : String? = null,
  @SerializedName("Status"         ) var Status         : String? = null,
  @SerializedName("message"        ) var message        : String? = null,
  @SerializedName("Value"          ) var Value          : String? = null

)