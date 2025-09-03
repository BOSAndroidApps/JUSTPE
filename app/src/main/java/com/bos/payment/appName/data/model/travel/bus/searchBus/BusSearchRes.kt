package com.bos.payment.appName.data.model.travel.bus.searchBus

import com.google.gson.annotations.SerializedName


data class BusSearchRes (

  @SerializedName("buses"           ) var buses          : ArrayList<Buses> = arrayListOf(),
  @SerializedName("response_Header" ) var responseHeader : ResponseHeader?  = ResponseHeader(),
  @SerializedName("search_Key"      ) var searchKey      : String?          = null,
  @SerializedName("statuss"         ) var statuss        : String?          = null,
  @SerializedName("message"         ) var message        : String?          = null,
  @SerializedName("value"           ) var value          : String?          = null

)