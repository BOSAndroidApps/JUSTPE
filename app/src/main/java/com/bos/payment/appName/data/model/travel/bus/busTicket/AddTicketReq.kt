package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName

data class AddTicketReq(
    @SerializedName("request_Id"  ) var requestId   : String? = null,
    @SerializedName("iP_Address"     ) var iPAddress      : String? = null,
    @SerializedName("imeI_Number"     ) var imeINumber      : String? = null,
    @SerializedName("booking_RefNo"    ) var bookingRefNo     : String? = null,
    @SerializedName("createdBy" ) var createdBy : String? = null,
    @SerializedName("registrationID" ) var registrationID : String? = null,
    @SerializedName("loginID" ) var loginID : String? = null,
    @SerializedName("apiRequest" ) var apiRequest : String? = null,
)
