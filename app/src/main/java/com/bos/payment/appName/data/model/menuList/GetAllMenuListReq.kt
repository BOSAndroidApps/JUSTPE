package com.bos.payment.appName.data.model.menuList

import com.google.gson.annotations.SerializedName


data class GetAllMenuListReq (

  @SerializedName("loginId"         ) var loginId         : String? = null,
  @SerializedName("applicationCode" ) var applicationCode : String? = null

)