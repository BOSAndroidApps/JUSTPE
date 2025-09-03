package com.bos.payment.appName.data.model.walletBalance.merchantBal

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("debitBalance" ) var debitBalance : String? = null

)