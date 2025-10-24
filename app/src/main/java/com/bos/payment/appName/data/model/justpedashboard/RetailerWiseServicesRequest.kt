package com.bos.payment.appName.data.model.justpedashboard

import com.google.gson.annotations.SerializedName

data class RetailerWiseServicesRequest(
    @SerializedName("retailercode")
    var retailerCode: String,

    @SerializedName("merchantcode")
    var merchantCode: String
)
