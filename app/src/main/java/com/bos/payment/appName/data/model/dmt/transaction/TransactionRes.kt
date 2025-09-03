package com.bos.payment.appName.data.model.dmt.transaction

import com.google.gson.annotations.SerializedName


data class TransactionRes (

  @SerializedName("status"          ) var status         : Boolean? = null,
  @SerializedName("response_code"   ) var responseCode   : Int?     = null,
  @SerializedName("ackno"           ) var ackno          : String?  = null,
  @SerializedName("utr"             ) var utr            : String?  = null,
  @SerializedName("txn_status"      ) var txnStatus      : Int?     = null,
  @SerializedName("benename"        ) var benename       : String?  = null,
  @SerializedName("remarks"         ) var remarks        : String?  = null,
  @SerializedName("message"         ) var message        : String?  = null,
  @SerializedName("remitter"        ) var remitter       : String?  = null,
  @SerializedName("account_number"  ) var accountNumber  : String?  = null,
  @SerializedName("txn_amount"      ) var txnAmount      : String?  = null,
  @SerializedName("txnReferanceID"  ) var txnReferanceID : String?  = null,
  @SerializedName("Registration_id" ) var RegistrationId : String?  = null,
  @SerializedName("Status"          ) var Status         : String?  = null,
  @SerializedName("Value"           ) var Value          : String?  = null

)