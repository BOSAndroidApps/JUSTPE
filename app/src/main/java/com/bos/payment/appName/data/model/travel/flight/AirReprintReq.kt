package com.bos.payment.appName.data.model.travel.flight

import com.google.gson.annotations.SerializedName

data class AirReprintReq(
    @SerializedName("booking_RefNo")
    var BookingRefNo  : String?,
    @SerializedName("airline_PNR")
    var airlinePNR  : String?,
    @SerializedName("iP_Address")
    var ipAddress  : String?,
    @SerializedName("request_Id")
    var requestId  : String?,
    @SerializedName("imeI_Number")
    var imeNumber  : String?,
    @SerializedName("registrationID")
    var registerId  : String?,
)
