package com.bos.payment.appName.data.model.otp

import com.google.gson.annotations.SerializedName


data class OtpSubmitReq (

  @SerializedName("GroupId"        ) var GroupId        : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("OTPNumber"      ) var OTPNumber      : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)