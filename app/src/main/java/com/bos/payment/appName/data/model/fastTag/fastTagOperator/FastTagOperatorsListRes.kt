package com.bos.payment.appName.data.model.fastTag.fastTagOperator

import com.google.gson.annotations.SerializedName


data class FastTagOperatorsListRes (

  @SerializedName("response_code" ) var responseCode : Int?            = null,
  @SerializedName("status"        ) var status       : Boolean?        = null,
  @SerializedName("data"          ) var data         : ArrayList<Data> = arrayListOf(),
  @SerializedName("message"       ) var message      : String?         = null

)