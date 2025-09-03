package com.bos.payment.appName.data.model.fastTag.customerDetails

import com.google.gson.annotations.SerializedName


data class BillFetchDetails (

  @SerializedName("billAmount"    ) var billAmount    : String?  = null,
  @SerializedName("billnetamount" ) var billnetamount : String?  = null,
  @SerializedName("dueDate"       ) var dueDate       : String?  = null,
  @SerializedName("maxBillAmount" ) var maxBillAmount : String?  = null,
  @SerializedName("acceptPayment" ) var acceptPayment : Boolean? = null,
  @SerializedName("acceptPartPay" ) var acceptPartPay : Boolean? = null,
  @SerializedName("cellNumber"    ) var cellNumber    : String?  = null,
  @SerializedName("userName"      ) var userName      : String?  = null

)