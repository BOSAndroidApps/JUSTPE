package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GenerateQRCodeReq(
    @SerializedName("RegistrationID")
    var RegistrationId : String ?,

    @SerializedName("mobileNumber")
    var mobileNumber : String ?,

    @SerializedName("MerchantCode")
    var merchantCode : String ?

)
