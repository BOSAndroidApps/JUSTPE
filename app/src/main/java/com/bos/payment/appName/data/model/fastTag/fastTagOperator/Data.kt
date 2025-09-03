package com.bos.payment.appName.data.model.fastTag.fastTagOperator

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("id"          ) var id          : String? = null,
  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("category"    ) var category    : String? = null,
  @SerializedName("viewbill"    ) var viewbill    : String? = null,
  @SerializedName("regex"       ) var regex       : String? = null,
  @SerializedName("displayname" ) var displayname : String? = null,
  @SerializedName("ad1_regex"   ) var ad1Regex    : String? = null

)