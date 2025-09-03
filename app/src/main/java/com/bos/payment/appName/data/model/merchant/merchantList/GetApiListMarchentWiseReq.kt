package com.bos.payment.appName.data.model.merchant.merchantList

import com.google.gson.annotations.SerializedName


data class GetApiListMarchentWiseReq (

  @SerializedName("merchantID" ) var MarchentID : String? = null

)