package com.bos.payment.appName.data.model.recharge.qrCode

import com.google.gson.annotations.SerializedName


data class GenerateQRCodeReq (

  @SerializedName("RegistrationID" ) var RegistrationID : String? = null

)