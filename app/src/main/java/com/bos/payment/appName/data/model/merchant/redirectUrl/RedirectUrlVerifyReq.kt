package com.bos.payment.appName.data.model.merchant.redirectUrl

import com.google.gson.annotations.SerializedName


data class RedirectUrlVerifyReq (

  @SerializedName("url" ) var url : String? = null

)