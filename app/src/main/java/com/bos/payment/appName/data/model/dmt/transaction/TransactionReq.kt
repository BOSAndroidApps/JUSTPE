package com.bos.payment.appName.data.model.dmt.transaction

import com.google.gson.annotations.SerializedName


data class TransactionReq (

  @SerializedName("MobileNumber"   ) var MobileNumber   : String? = null,
  @SerializedName("Pincode"        ) var Pincode        : String? = null,
  @SerializedName("Address"        ) var Address        : String? = null,
  @SerializedName("DOB"            ) var DOB            : String? = null,
  @SerializedName("BeneID"         ) var BeneID         : String? = null,
  @SerializedName("TxnType"        ) var TxnType        : String? = null,
  @SerializedName("Amount"         ) var Amount         : String? = null,
  @SerializedName("Stateresp"      ) var Stateresp      : String? = null,
  @SerializedName("OTP"            ) var OTP            : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)