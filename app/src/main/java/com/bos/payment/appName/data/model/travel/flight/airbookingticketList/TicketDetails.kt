package com.bos.payment.appName.data.model.travel.flight.airbookingticketList

import com.google.gson.annotations.SerializedName

data class TicketDetails(@SerializedName("segemtWiseChanges")
                         val segemtWiseChanges: List<SegemtWiseChangesItem>?,
                         @SerializedName("ticket_Number")
                         val ticketNumber: String = "",
                         @SerializedName("flight_Id")
                         val flightId: String = "",
                         @SerializedName("pax_ID")
                         val paxID: String = "")