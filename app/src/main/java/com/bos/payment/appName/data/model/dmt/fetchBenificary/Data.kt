package com.bos.payment.appName.data.model.dmt.fetchBenificary

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Data (

  @SerializedName("bene_id"  ) var beneId   : String?  = null,
  @SerializedName("bankid"   ) var bankid   : String?  = null,
  @SerializedName("bankname" ) var bankname : String?  = null,
  @SerializedName("name"     ) var name     : String?  = null,
  @SerializedName("accno"    ) var accno    : String?  = null,
  @SerializedName("ifsc"     ) var ifsc     : String?  = null,
  @SerializedName("verified" ) var verified : String?  = null,
  @SerializedName("banktype" ) var banktype : String?  = null,
  @SerializedName("paytm"    ) var paytm    : Boolean? = null

): Serializable