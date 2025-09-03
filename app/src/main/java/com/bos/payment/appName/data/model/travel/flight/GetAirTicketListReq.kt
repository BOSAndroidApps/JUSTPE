package com.bos.payment.appName.data.model.travel.flight

import com.google.gson.annotations.SerializedName

data class GetAirTicketListReq(
    @SerializedName("loginID")
    var loginId  : String?
)
