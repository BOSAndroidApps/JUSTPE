package com.bos.payment.appName.data.model.fastTag.billPayment

import com.google.gson.annotations.SerializedName


data class BillPaymentPaybillReq (

  @SerializedName("operator"       ) var operator       : String?    = null,
  @SerializedName("canumber"       ) var canumber       : String?    = null,
  @SerializedName("amount"         ) var amount         : String?    = null,
  @SerializedName("referenceid"    ) var referenceid    : String?    = null,
  @SerializedName("latitude"       ) var latitude       : String?    = null,
  @SerializedName("longitude"      ) var longitude      : String?    = null,
  @SerializedName("mode"           ) var mode           : String?    = null,
  @SerializedName("RegistrationID" ) var RegistrationID : String?    = null,
  @SerializedName("bill_fetch"     ) var billFetch      : BillFetch? = BillFetch()

)