package com.bos.payment.appName.data.model.walletBalance.walletBalanceCal

import com.google.gson.annotations.SerializedName


data class GetBalanceReq (

  @SerializedName("parmUser" ) var parmUser : String? = null,
  @SerializedName("flag"     ) var flag     : String? = null

)