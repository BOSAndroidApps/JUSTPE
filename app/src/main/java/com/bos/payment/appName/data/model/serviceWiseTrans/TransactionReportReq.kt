package com.bos.payment.appName.data.model.serviceWiseTrans

import com.google.gson.annotations.SerializedName


data class TransactionReportReq (

  @SerializedName("parmFromDate"      ) var parmFromDate      : String? = null,
  @SerializedName("parmToDate"        ) var parmToDate        : String? = null,
  @SerializedName("parmFlag"          ) var parmFlag          : String? = null,
  @SerializedName("parmFla2"          ) var parmFla2          : String? = null,
  @SerializedName("parmUser"          ) var parmUser          : String? = null,
  @SerializedName("paramMerchantCode" ) var paramMerchantCode : String? = null

)