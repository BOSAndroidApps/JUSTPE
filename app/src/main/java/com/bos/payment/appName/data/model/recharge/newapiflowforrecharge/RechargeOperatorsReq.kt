package com.bos.payment.appName.data.model.recharge.newapiflowforrecharge

import com.google.gson.annotations.SerializedName

data class RechargeOperatorsReq(
    @SerializedName("RegistrationID")
    var registrationID : String ?,

    @SerializedName("displayName")
    var displayName : String

)
