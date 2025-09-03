package com.bos.payment.appName.data.model.stateDistrict

import com.google.gson.annotations.SerializedName


data class GetStateRes (

  @SerializedName("RID"        ) var RID       : Int?    = null,
  @SerializedName("statecode"  ) var statecode : String? = null,
  @SerializedName("state_name" ) var stateName : String? = null,
  @SerializedName("Status"     ) var Status    : String? = null,
  @SerializedName("message"    ) var message   : String? = null,
  @SerializedName("Value"      ) var Value     : String? = null

)