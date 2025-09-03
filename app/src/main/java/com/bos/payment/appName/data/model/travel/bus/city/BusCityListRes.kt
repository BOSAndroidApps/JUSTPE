package com.bos.payment.appName.data.model.travel.bus.city

import com.google.gson.annotations.SerializedName


data class BusCityListRes (

    @SerializedName("cityDetails"     ) var cityDetails    : ArrayList<CityDetails> = arrayListOf(),
    @SerializedName("response_Header" ) var responseHeader : ResponseHeader?        = ResponseHeader(),
    @SerializedName("statuss"         ) var statuss        : String?                = null,
    @SerializedName("message"         ) var message        : String?                = null,
    @SerializedName("value"           ) var value          : String?                = null

)