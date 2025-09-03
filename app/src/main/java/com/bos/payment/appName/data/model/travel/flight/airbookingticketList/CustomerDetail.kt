package com.bos.payment.appName.data.model.travel.flight.airbookingticketList

import com.google.gson.annotations.SerializedName

data class CustomerDetail(@SerializedName("customer_Email")
                          val customerEmail: String = "",
                          @SerializedName("gstHolderName")
                          val gstHolderName: String = "",
                          @SerializedName("customer_Mobile")
                          val customerMobile: String = "",
                          @SerializedName("customer_Name")
                          val customerName: String = "",
                          @SerializedName("gsT_Number")
                          val gsTNumber: String = "",
                          @SerializedName("gstAddress")
                          val gstAddress: String = "")