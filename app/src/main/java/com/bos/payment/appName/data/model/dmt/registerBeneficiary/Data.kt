package com.bos.payment.appName.data.model.dmt.registerBeneficiary

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("bene_id"  ) var beneId   : String? = null,
  @SerializedName("bankid"   ) var bankid   : String? = null,
  @SerializedName("bankname" ) var bankname : String? = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("accno"    ) var accno    : String? = null,
  @SerializedName("ifsc"     ) var ifsc     : String? = null,
  @SerializedName("verified" ) var verified : String? = null,
  @SerializedName("banktype" ) var banktype : String? = null,
  @SerializedName("status"   ) var status   : String? = null,
  @SerializedName("bank3"    ) var bank3    : String? = null

)