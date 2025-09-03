package com.bos.payment.appName.data.model.dmt.queryRemitters

import com.google.gson.annotations.SerializedName


data class QueryRemitterReq (

  @SerializedName("MobileNumber"   ) var MobileNumber   : String? = null,
  @SerializedName("Lattitude"      ) var Lattitude      : String? = null,
  @SerializedName("Longitude"      ) var Longitude      : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)