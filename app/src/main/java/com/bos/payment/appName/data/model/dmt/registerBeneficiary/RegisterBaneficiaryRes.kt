package com.bos.payment.appName.data.model.dmt.registerBeneficiary

import com.google.gson.annotations.SerializedName


data class RegisterBaneficiaryRes (

  @SerializedName("status"        ) var status       : Boolean? = null,
  @SerializedName("response_code" ) var responseCode : Int?     = null,
  @SerializedName("message"       ) var message      : String?  = null,
  @SerializedName("data"          ) var data         : Data?    = Data(),
  @SerializedName("Status"        ) var Status       : String?  = null,
  @SerializedName("Value"         ) var Value        : String?  = null

)