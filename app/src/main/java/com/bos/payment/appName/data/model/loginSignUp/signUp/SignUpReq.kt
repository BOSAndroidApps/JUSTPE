package com.bos.payment.appName.data.model.loginSignUp.signUp

import com.google.gson.annotations.SerializedName


data class SignUpReq (

  @SerializedName("agentType"       ) var agentType       : String? = null,
  @SerializedName("applicationType" ) var applicationType : String? = null,
  @SerializedName("userID"          ) var userID          : String? = null,
  @SerializedName("agentPassword"   ) var agentPassword   : String? = null,
  @SerializedName("firstName"       ) var firstName       : String? = null,
  @SerializedName("lastName"        ) var lastName        : String? = null,
  @SerializedName("mobileNo"        ) var mobileNo        : String? = null,
  @SerializedName("emailID"         ) var emailID         : String? = null,
  @SerializedName("companyCode"     ) var companyCode     : String? = null,
  @SerializedName("agencyName"      ) var agencyName      : String? = null,
  @SerializedName("refrenceType"    ) var refrenceType    : String? = null,
  @SerializedName("ref_Code"        ) var refCode         : String? = null,
  @SerializedName("refrenceID"      ) var refrenceID      : String? = null,
  @SerializedName("ipAddress"       ) var ipAddress       : String? = null


)