package com.bos.payment.appName.data.model.travel.bus.history

import com.google.gson.annotations.SerializedName


data class BusHistoryReq (

  @SerializedName("fromdate"       ) var fromdate       : String? = null,
  @SerializedName("month"          ) var month          : String? = null,
  @SerializedName("todate"         ) var todate         : String? = null,
  @SerializedName("type"           ) var type           : Int?    = null,
  @SerializedName("year"           ) var year           : String? = null,
  @SerializedName("iP_Address"     ) var iPAddress      : String? = null,
  @SerializedName("request_Id"     ) var requestId      : String? = null,
  @SerializedName("imeI_Number"    ) var imeINumber     : String? = null,
  @SerializedName("registrationID" ) var registrationID : String? = null

)