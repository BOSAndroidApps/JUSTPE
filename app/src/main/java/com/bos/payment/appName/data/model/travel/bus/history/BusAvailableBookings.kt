package com.bos.payment.appName.data.model.travel.bus.history

import com.google.gson.annotations.SerializedName


data class BusAvailableBookings (

  @SerializedName("booking_RefNo"       ) var bookingRefNo        : String? = null,
  @SerializedName("cancellationCharges" ) var cancellationCharges : Int?    = null,
  @SerializedName("customerMobile"      ) var customerMobile      : String? = null,
  @SerializedName("from_City"           ) var fromCity            : String? = null,
  @SerializedName("gatewayCharges"      ) var gatewayCharges      : Int?    = null,
  @SerializedName("invoiceNumber"       ) var invoiceNumber       : String? = null,
  @SerializedName("operator_Name"       ) var operatorName        : String? = null,
  @SerializedName("paX_Name"            ) var paXName             : String? = null,
  @SerializedName("statusDesc"          ) var statusDesc          : String? = null,
  @SerializedName("ticketingDate"       ) var ticketingDate       : String? = null,
  @SerializedName("to_City"             ) var toCity              : String? = null,
  @SerializedName("total_Amount"        ) var totalAmount         : Int?    = null,
  @SerializedName("transport_PNR"       ) var transportPNR        : String? = null,
  @SerializedName("travelDate"          ) var travelDate          : String? = null

)