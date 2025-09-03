package com.bos.payment.appName.data.model.travel.flight.airbookingticketList

import com.google.gson.annotations.SerializedName

data class FreeBaggage(@SerializedName("check_In_Baggage")
                       val checkInBaggage: String = "",
                       @SerializedName("hand_Baggage")
                       val handBaggage: String = "",
                       @SerializedName("displayRemarks")
                       val displayRemarks: String = "")