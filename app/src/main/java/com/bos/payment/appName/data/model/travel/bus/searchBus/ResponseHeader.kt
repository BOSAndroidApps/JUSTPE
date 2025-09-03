package com.bos.payment.appName.data.model.travel.bus.searchBus

import com.google.gson.annotations.SerializedName


data class ResponseHeader (

  @SerializedName("error_Code"           ) var errorCode           : String? = null,
  @SerializedName("error_Desc"           ) var errorDesc           : String? = null,
  @SerializedName("error_InnerException" ) var errorInnerException : String? = null,
  @SerializedName("request_Id"           ) var requestId           : String? = null,
  @SerializedName("status_Id"            ) var statusId            : String? = null

)