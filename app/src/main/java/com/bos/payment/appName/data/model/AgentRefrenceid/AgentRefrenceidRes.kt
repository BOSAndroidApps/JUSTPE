package com.bos.payment.appName.data.model.AgentRefrenceid

import com.google.gson.annotations.SerializedName


data class AgentRefrenceidRes (

  @SerializedName("DIS_RegistrationId" ) var DISRegistrationId : String? = null,
  @SerializedName("MD_RegistrationId"  ) var MDRegistrationId  : String? = null,
  @SerializedName("Status"             ) var Status            : Boolean? = null,
  @SerializedName("message"            ) var message           : String? = null,
  @SerializedName("Value"              ) var Value             : String? = null

)