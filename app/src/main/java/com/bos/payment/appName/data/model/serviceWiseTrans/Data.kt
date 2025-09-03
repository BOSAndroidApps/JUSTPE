package com.bos.payment.appName.data.model.serviceWiseTrans

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("sNo"              ) var sNo              : String? = null,
  @SerializedName("transactionno"    ) var transactionno    : String? = null,
  @SerializedName("upiRefID"         ) var upiRefID         : String? = null,
  @SerializedName("date"             ) var date             : String? = null,
  @SerializedName("time"             ) var time             : String? = null,
  @SerializedName("transferfrom"     ) var transferfrom     : String? = null,
  @SerializedName("transferto"       ) var transferto       : String? = null,
  @SerializedName("servicE_TYPE"     ) var servicETYPE      : String? = null,
  @SerializedName("cr"               ) var cr               : String? = null,
  @SerializedName("dr"               ) var dr               : String? = null,
  @SerializedName("retailerComm"     ) var retailerComm     : String? = null,
  @SerializedName("serviceCh"        ) var serviceCh        : String? = null,
  @SerializedName("gst"              ) var gst              : String? = null,
  @SerializedName("serviceCh_Gst"    ) var serviceChGst     : String? = null,
  @SerializedName("tranAmt"          ) var tranAmt          : String? = null,
  @SerializedName("remarks"          ) var remarks          : String? = null,
  @SerializedName("status"           ) var status           : String? = null,
  @SerializedName("ipAddress"        ) var ipAddress        : String? = null,
  @SerializedName("id"               ) var id               : String? = null,
  @SerializedName("flagRemarks"      ) var flagRemarks      : String? = null,
  @SerializedName("payoutExternalID" ) var payoutExternalID : String? = null

)