package com.bos.payment.appName.data.model.merchant.apiServiceCharge

import com.google.gson.annotations.SerializedName


data class GetPayoutCommercialRes (

  @SerializedName("isSuccess"     ) var isSuccess     : Boolean?        = null,
  @SerializedName("returnMessage" ) var returnMessage : String?         = null,
  @SerializedName("returnCode"    ) var returnCode    : String?         = null,
  @SerializedName("data"          ) var data          : ArrayList<Data> = arrayListOf()


)