package com.bos.payment.appName.data.model.merchant.activeInActiveStatus

import com.google.gson.annotations.SerializedName


data class GetAPIActiveInactiveStatusReq (

  @SerializedName("RegistrationId" ) var RegistrationId : String? = null,
  @SerializedName("CompanyCode"    ) var CompanyCode    : String? = null

)