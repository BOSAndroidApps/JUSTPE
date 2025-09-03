package com.bos.payment.appName.data.model.walletBalance.walletBalanceCal

import com.google.gson.annotations.SerializedName


data class WalletBalanceReq (

  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)