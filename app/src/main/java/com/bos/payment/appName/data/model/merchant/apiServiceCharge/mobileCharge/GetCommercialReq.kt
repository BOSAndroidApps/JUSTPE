package com.bos.payment.appName.data.model.merchant.apiServiceCharge.mobileCharge

import com.google.gson.annotations.SerializedName


data class GetCommercialReq (

  @SerializedName("txtslabamtfrom"  ) var txtslabamtfrom  : Int?    = null,
  @SerializedName("txtslabamtto"    ) var txtslabamtto    : Int?    = null,
  @SerializedName("merchant"        ) var merchant        : String? = null,
  @SerializedName("productId"       ) var productId       : String? = null,
  @SerializedName("cantentType"     ) var cantentType     : String? = null,
  @SerializedName("operatorsTypeID" ) var operatorsTypeID : String? = null

)