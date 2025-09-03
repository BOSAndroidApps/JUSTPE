package com.bos.payment.appName.data.model.transferAMountToAgent

import com.google.gson.annotations.SerializedName


data class TransferAmountToAgentsReq (

  @SerializedName("transferFrom"                ) var transferFrom             : String? = null,
  @SerializedName("transferTo"                  ) var transferTo               : String? = null,
  @SerializedName("transferAmt"                 ) var transferAmt              : String?    = null,
  @SerializedName("remark"                      ) var remark                   : String? = null,
  @SerializedName("transferFromMsg"             ) var transferFromMsg          : String? = null,
  @SerializedName("transferToMsg"               ) var transferToMsg            : String? = null,
  @SerializedName("amount_Type"                 ) var amountType               : String? = null,
  @SerializedName("actual_Transaction_Amount"   ) var actualTransactionAmount  : String?    = null,
  @SerializedName("transIpAddress"              ) var transIpAddress           : String? = null,
  @SerializedName("parmUserName"                ) var parmUserName             : String? = null,
  @SerializedName("merchantCode"                ) var merchantCode             : String? = null,
  @SerializedName("services_charge_Amt"         ) var servicesChargeAmt        : String?    = null,
  @SerializedName("services_charge_GSTAmt"      ) var servicesChargeGSTAmt     : String?    = null,
  @SerializedName("services_charge_Without_GST" ) var servicesChargeWithoutGST : String?    = null,
  @SerializedName("customer_virtual_address"    ) var customerVirtualAddress   : String? = null,
  @SerializedName("retailerCommissionAmt"       ) var retailerCommissionAmt    : String?    = null,
  @SerializedName("retailerId"                  ) var retailerId               : String? = null,
  @SerializedName("paymentMode"                 ) var paymentMode              : String? = null,
  @SerializedName("depositBankName"             ) var depositBankName          : String? = null,
  @SerializedName("branchCode_ChecqueNo"        ) var branchCodeChecqueNo      : String? = null,
  @SerializedName("apporvedStatus"              ) var apporvedStatus           : String? = null,
  @SerializedName("registrationId"              ) var registrationId           : String? = null,
  @SerializedName("benfiid"                     ) var benfiid                  : String? = null,
  @SerializedName("accountHolder"               ) var accountHolder            : String? = null,
  @SerializedName("flag"                        ) var flag                     : String? = null

)