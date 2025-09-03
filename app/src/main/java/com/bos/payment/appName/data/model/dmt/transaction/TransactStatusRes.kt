package com.bos.payment.appName.data.model.dmt.transaction

import com.google.gson.annotations.SerializedName


data class TransactStatusRes (

  @SerializedName("status"         ) var status         : Boolean? = null,
  @SerializedName("response_code"  ) var responseCode   : Int?     = null,
  @SerializedName("utr"            ) var utr            : String?  = null,
  @SerializedName("amount"         ) var amount         : String?  = null,
  @SerializedName("ackno"          ) var ackno          : String?  = null,
  @SerializedName("referenceid"    ) var referenceid    : String?  = null,
  @SerializedName("account"        ) var account        : String?  = null,
  @SerializedName("txn_status"     ) var txnStatus      : String?  = null,
  @SerializedName("message"        ) var message        : String?  = null,
  @SerializedName("customercharge" ) var customercharge : String?  = null,
  @SerializedName("gst"            ) var gst            : String?  = null,
  @SerializedName("bc_share"       ) var bcShare        : String?  = null,
  @SerializedName("tds"            ) var tds            : String?  = null,
  @SerializedName("netcommission"  ) var netcommission  : String?  = null,
  @SerializedName("daterefunded"   ) var daterefunded   : String?  = null,
  @SerializedName("refundtxnid"    ) var refundtxnid    : String?  = null

)