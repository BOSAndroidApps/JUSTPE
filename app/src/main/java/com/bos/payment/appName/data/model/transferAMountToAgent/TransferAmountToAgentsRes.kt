package com.bos.payment.appName.data.model.transferAMountToAgent

import com.google.gson.annotations.SerializedName


data class TransferAmountToAgentsRes (

  @SerializedName("isSuccess"     ) var isSuccess     : Boolean? = null,
  @SerializedName("returnMessage" ) var returnMessage : String?  = null,
  @SerializedName("returnCode"    ) var returnCode    : String?  = null,
  @SerializedName("data"          ) var data          : Data?    = Data()


)