package com.bos.payment.appName.data.model.loginSignUp

import com.google.gson.annotations.SerializedName


data class ValidateReferenceIdRes (

  @SerializedName("AgentType" ) var AgentType : String? = null,
  @SerializedName("Name"      ) var Name      : String? = null,
  @SerializedName("Status"    ) var Status    : Boolean? = null,
  @SerializedName("message"   ) var message   : String? = null,
  @SerializedName("Value"     ) var Value     : String? = null

)