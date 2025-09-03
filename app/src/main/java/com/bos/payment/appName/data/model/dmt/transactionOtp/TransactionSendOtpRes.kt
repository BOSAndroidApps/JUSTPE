package com.bos.payment.appName.data.model.dmt.transactionOtp

import com.google.gson.annotations.SerializedName


data class TransactionSendOtpRes (

  @SerializedName("status"        ) var status       : Boolean? = null,
  @SerializedName("response_code" ) var responseCode : Int?     = null,
  @SerializedName("message"       ) var message      : String?  = null,
  @SerializedName("stateresp"     ) var stateresp    : String?  = null,
  @SerializedName("Status"        ) var Status       : String?  = null,
  @SerializedName("Value"         ) var Value        : String?  = null

)