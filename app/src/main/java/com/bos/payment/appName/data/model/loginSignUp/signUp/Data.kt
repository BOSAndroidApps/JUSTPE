package com.bos.payment.appName.data.model.loginSignUp.signUp

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("userID"          ) var userID          : String? = null,
  @SerializedName("agentPassword"   ) var agentPassword   : String? = null,
  @SerializedName("agentType"       ) var agentType       : String? = null,
  @SerializedName("applicationType" ) var applicationType : String? = null,
  @SerializedName("firstName"       ) var firstName       : String? = null,
  @SerializedName("lastName"        ) var lastName        : String? = null,
  @SerializedName("emailId"         ) var emailId         : String? = null,
  @SerializedName("mobileNo"        ) var mobileNo        : String? = null

)