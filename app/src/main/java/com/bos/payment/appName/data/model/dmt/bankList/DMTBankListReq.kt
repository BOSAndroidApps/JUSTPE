package com.bos.payment.appName.data.model.dmt.bankList

import com.google.gson.annotations.SerializedName


data class DMTBankListReq (

  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)