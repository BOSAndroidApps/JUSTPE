package com.bos.payment.appName.data.model.dmt.fetchBenificary

import com.google.gson.annotations.SerializedName


data class FetchBeneficiaryReq (

  @SerializedName("MobileNo"         ) var mobile         : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)