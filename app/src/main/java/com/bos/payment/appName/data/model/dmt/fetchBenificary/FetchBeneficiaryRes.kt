package com.bos.payment.appName.data.model.dmt.fetchBenificary

import com.google.gson.annotations.SerializedName


data class FetchBeneficiaryRes (

  @SerializedName("status"        ) var status       : Boolean?        = null,
  @SerializedName("response_code" ) var responseCode : Int?            = null,
  @SerializedName("message"       ) var message      : String?         = null,
  @SerializedName("data"          ) var data         : ArrayList<Data> = arrayListOf()

)