package com.bos.payment.appName.data.model.fastTag.recharge

import com.google.gson.annotations.SerializedName


data class BillFetch (

  @SerializedName("billAmount"    ) var billAmount    : String?  = null,
  @SerializedName("billnetamount" ) var billnetamount : String?  = null,
  @SerializedName("minBillAmount" ) var minBillAmount : String?  = null,
  @SerializedName("acceptPayment" ) var acceptPayment : Boolean? = null,
  @SerializedName("acceptPartPay" ) var acceptPartPay : Boolean? = null,
  @SerializedName("cellNumber"    ) var cellNumber    : String?  = null,
  @SerializedName("userName"      ) var userName      : String?  = null

)