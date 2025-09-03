package com.bos.payment.appName.data.model.dmt.queryRemitters

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class QueryRemitterRes (

  @SerializedName("status"        ) var status       : Boolean? = null,
  @SerializedName("response_code" ) var responseCode : Int?     = null,
  @SerializedName("message"       ) var message      : String?  = null,
  @SerializedName("data"          ) var data         : Data?    = Data(),
  @SerializedName("Status"        ) var Status       : String?  = null,
  @SerializedName("Value"         ) var Value        : String?  = null


) : Serializable