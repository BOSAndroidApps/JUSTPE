package com.bos.payment.appName.data.model.dmt.registerBeneficiary

import com.google.gson.annotations.SerializedName


data class RegisterBaneficiaryReq (

  @SerializedName("MobileNumber"   ) var MobileNumber   : String? = null,
  @SerializedName("BeneName"       ) var BeneName       : String? = null,
  @SerializedName("BankID"         ) var BankID         : String? = null,
  @SerializedName("AccountNumber"  ) var AccountNumber  : String? = null,
  @SerializedName("IfscCode"       ) var IfscCode       : String? = null,
  @SerializedName("DOB"            ) var DOB            : String? = null,
  @SerializedName("Address"        ) var Address        : String? = null,
  @SerializedName("Pincode"        ) var Pincode        : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)