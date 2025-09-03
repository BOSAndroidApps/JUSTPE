package com.bos.payment.appName.data.model.walletBalance.walletBalanceCal

import com.google.gson.annotations.SerializedName


data class WalletBalanceRes (

  @SerializedName("ActualBalance_virtual" ) var ActualBalance : String? = null,
  @SerializedName("HoldAmount_virtual"    ) var HoldAmount    : String? = null,
  @SerializedName("CreditAmount_virtual"  ) var CreditAmount  : String? = null,
  @SerializedName("MerchantBalance"       ) var MerchantBalance  : String? = null,
  @SerializedName("MarchentId"            ) var MarchentId    : String? = null,
  @SerializedName("Status"                ) var Status        : Boolean? = null,
  @SerializedName("message"               ) var message       : String? = null,
  @SerializedName("Value"                 ) var Value         : String? = null
)