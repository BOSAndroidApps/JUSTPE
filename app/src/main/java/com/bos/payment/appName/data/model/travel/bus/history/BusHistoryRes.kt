package com.bos.payment.appName.data.model.travel.bus.history

import com.google.gson.annotations.SerializedName


data class BusHistoryRes (

  @SerializedName("busAvailableBookings" ) var busAvailableBookings : ArrayList<BusAvailableBookings> = arrayListOf(),
  @SerializedName("response_Header"      ) var responseHeader       : ResponseHeader?                 = ResponseHeader(),
  @SerializedName("statuss"              ) var statuss              : String?                         = null,
  @SerializedName("message"              ) var message              : String?                         = null,
  @SerializedName("value"                ) var value                : String?                         = null

)