package com.bos.payment.appName.data.model.walletBalance

import com.google.gson.annotations.SerializedName


data class ReturnWalletBalanceReq (

  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("CompanyCode" ) var CompanyCode : String? = null

)