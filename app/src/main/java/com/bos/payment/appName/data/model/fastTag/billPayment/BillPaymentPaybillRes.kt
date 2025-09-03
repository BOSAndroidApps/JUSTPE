package com.bos.payment.appName.data.model.fastTag.billPayment

import com.google.gson.annotations.SerializedName


data class BillPaymentPaybillRes (

  @SerializedName("status"        ) var status       : Boolean? = null,
  @SerializedName("response_code" ) var responseCode : Int?     = null,
  @SerializedName("operatorid"    ) var operatorid   : String?  = null,
  @SerializedName("ackno"         ) var ackno        : Int?     = null,
  @SerializedName("opening"       ) var opening      : String?  = null,
  @SerializedName("closing"       ) var closing      : String?  = null,
  @SerializedName("message"       ) var message      : String?  = null

)