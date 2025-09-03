package com.bos.payment.appName.data.model.travel.flight.airbookingticketList

import com.google.gson.annotations.SerializedName

data class AirportTaxesItem(@SerializedName("tax_Desc")
                            val taxDesc: String = "",
                            @SerializedName("tax_Code")
                            val taxCode: String = "",
                            @SerializedName("tax_Amount")
                            val taxAmount: String = "")