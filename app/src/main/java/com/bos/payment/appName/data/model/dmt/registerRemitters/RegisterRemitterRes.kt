package com.bos.payment.appName.data.model.dmt.registerRemitters

import com.google.gson.annotations.SerializedName


data class RegisterRemitterRes (

  @SerializedName("fname"       ) var fname      : String? = null,
  @SerializedName("lname"       ) var lname      : String? = null,
  @SerializedName("mobile"      ) var mobile     : String? = null,
  @SerializedName("status"      ) var status     : Boolean? = null,
  @SerializedName("bank3_limit" ) var bank3Limit : Int?    = null,
  @SerializedName("bank2_limit" ) var bank2Limit : Int?    = null,
  @SerializedName("bank1_limit" ) var bank1Limit : Int?    = null,
  @SerializedName("Status"      ) var Status     : String? = null,
  @SerializedName("message"     ) var message    : String? = null,
  @SerializedName("Value"       ) var Value      : String? = null

)