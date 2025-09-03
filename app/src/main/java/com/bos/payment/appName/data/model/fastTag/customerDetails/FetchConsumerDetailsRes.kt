package com.example.example

import com.bos.payment.appName.data.model.fastTag.customerDetails.BillFetchDetails
import com.google.gson.annotations.SerializedName


data class FetchConsumerDetailsRes (

  @SerializedName("response_code" ) var responseCode : Int?       = null,
  @SerializedName("status"        ) var status       : Boolean?   = null,
  @SerializedName("amount"        ) var amount       : String?    = null,
  @SerializedName("name"          ) var name         : String?    = null,
  @SerializedName("duedate"       ) var duedate      : String?    = null,
  @SerializedName("bill_fetch"    ) var billFetchDetails    : BillFetchDetails? = BillFetchDetails(),
  @SerializedName("message"       ) var message      : String?    = null

)