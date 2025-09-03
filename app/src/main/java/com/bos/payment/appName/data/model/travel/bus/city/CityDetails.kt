package com.bos.payment.appName.data.model.travel.bus.city

import com.google.gson.annotations.SerializedName


data class CityDetails (

  @SerializedName("cityID"   ) var cityID   : Int?    = null,
  @SerializedName("cityName" ) var cityName : String? = null

)