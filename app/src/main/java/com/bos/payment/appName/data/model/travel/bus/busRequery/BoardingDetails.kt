package com.bos.payment.appName.data.model.travel.bus.busRequery

import com.google.gson.annotations.SerializedName


data class BoardingDetails (

  @SerializedName("boarding_Address"  ) var boardingAddress  : String? = null,
  @SerializedName("boarding_Contact"  ) var boardingContact  : String? = null,
  @SerializedName("boarding_Id"       ) var boardingId       : String? = null,
  @SerializedName("boarding_Landmark" ) var boardingLandmark : String? = null,
  @SerializedName("boarding_Name"     ) var boardingName     : String? = null,
  @SerializedName("boarding_Time"     ) var boardingTime     : String? = null,
  @SerializedName("filler"            ) var filler           : String? = null

)