package com.bos.payment.appName.data.model.merchant.merchantList

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("featureCode" ) var featureCode : String? = null,
  @SerializedName("featureName" ) var featureName : String? = null

)