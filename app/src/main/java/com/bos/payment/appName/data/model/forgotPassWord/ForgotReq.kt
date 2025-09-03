package com.bos.payment.appName.data.model.forgotPassWord

import com.google.gson.annotations.SerializedName


data class ForgotReq (

  @SerializedName("GroupId"        ) var GroupId        : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("EmailID"        ) var EmailID        : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)