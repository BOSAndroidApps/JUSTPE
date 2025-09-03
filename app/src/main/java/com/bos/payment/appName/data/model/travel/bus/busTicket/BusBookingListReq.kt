package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName

data class BusBookingListReq(
    @SerializedName("loginID" ) var loginID : String? = null,
    @SerializedName("startDate" ) val startDate: String? = null,
    @SerializedName("endDate"   ) val endDate: String? = null,

)
