package com.bos.payment.appName.data.model.dmt.bankList

import com.google.gson.annotations.SerializedName


data class DMTBankListRes (

  @SerializedName("BankId"   ) var BankId   : Int?    = null,
  @SerializedName("BankName" ) var BankName : String? = null,
  @SerializedName("Status"   ) var Status   : Boolean? = null,
  @SerializedName("message"  ) var message  : String? = null,
  @SerializedName("Value"    ) var Value    : String? = null

)