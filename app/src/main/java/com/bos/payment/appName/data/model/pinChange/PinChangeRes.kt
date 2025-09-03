package com.bos.payment.appName.data.model.pinChange

import com.google.gson.annotations.SerializedName


data class PinChangeRes (

  @SerializedName("AgentType"      ) var AgentType      : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null,
  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("TransactionPin" ) var TransactionPin : String? = null,
  @SerializedName("Status"         ) var Status         : String? = null,
  @SerializedName("message"        ) var message        : String? = null,
  @SerializedName("Value"          ) var Value          : String? = null

)