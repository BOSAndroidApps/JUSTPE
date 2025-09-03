package com.bos.payment.appName.data.model.dmt.transactionOtp

import com.google.gson.annotations.SerializedName


data class TransactionSendOtpReq (

  @SerializedName("MobileNumber"   ) var MobileNumber   : String? = null,
  @SerializedName("BeneID"         ) var BeneID         : String? = null,
  @SerializedName("Txntype"        ) var Txntype        : String? = null,
  @SerializedName("Amount"         ) var Amount         : String? = null,
  @SerializedName("Pincode"        ) var Pincode        : String? = null,
  @SerializedName("Address"        ) var Address        : String? = null,
  @SerializedName("DOB"            ) var DOB            : String? = null,
  @SerializedName("Latitude"       ) var Latitude       : String? = null,
  @SerializedName("Longitute"      ) var Longitute      : String? = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)