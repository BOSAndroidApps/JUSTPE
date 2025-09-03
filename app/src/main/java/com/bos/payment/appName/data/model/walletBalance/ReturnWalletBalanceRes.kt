package com.bos.payment.appName.data.model.walletBalance

import com.google.gson.annotations.SerializedName


data class ReturnWalletBalanceRes (

  @SerializedName("BalanceAmount" ) var BalanceAmount : Double?    = null,
  @SerializedName("Status"        ) var Status        : Boolean? = null,
  @SerializedName("message"       ) var message       : String? = null,
  @SerializedName("Value"         ) var Value         : String? = null

)