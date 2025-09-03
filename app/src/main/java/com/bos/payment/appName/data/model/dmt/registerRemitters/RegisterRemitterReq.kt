package com.bos.payment.appName.data.model.dmt.registerRemitters

import com.google.gson.annotations.SerializedName


data class RegisterRemitterReq (

  @SerializedName("mobile"         ) var mobile         : String? = null,
  @SerializedName("firstname"      ) var firstname      : String? = null,
  @SerializedName("lastname"       ) var lastname       : String? = null,
  @SerializedName("address"        ) var address        : String? = null,
  @SerializedName("otp"            ) var otp            : String? = null,
  @SerializedName("pincode"        ) var pincode        : String? = null,
  @SerializedName("stateresp"      ) var stateresp      : String? = null,
  @SerializedName("bank3_flag"     ) var bank3Flag      : String? = null,
  @SerializedName("dob"            ) var dob            : String? = null,
  @SerializedName("gst_state"      ) var gstState       : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null,
  @SerializedName("Status"         ) var Status         : String? = null,
  @SerializedName("message"        ) var message        : String? = null,
  @SerializedName("Value"          ) var Value          : String? = null

)