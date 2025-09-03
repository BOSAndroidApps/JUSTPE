package com.bos.payment.appName.data.model.idfcPayout

import com.google.gson.annotations.SerializedName


data class InitiateAuthGenericFundTransferAPIResp (

  @SerializedName("metaData"     ) var metaData     : MetaData?     = MetaData(),
  @SerializedName("resourceData" ) var resourceData : ResourceData? = ResourceData()

)