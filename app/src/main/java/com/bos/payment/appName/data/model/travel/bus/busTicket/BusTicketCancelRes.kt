package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName

data class BusTicketCancelRes(@SerializedName("response_Header")
                              val responseHeader: ResponseHeader,
                              @SerializedName("message")
                              val message: String = "",
                              @SerializedName("statuss")
                              val statuss: String = "",
                              @SerializedName("value")
                              val value: String = "")