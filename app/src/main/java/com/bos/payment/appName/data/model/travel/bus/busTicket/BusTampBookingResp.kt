package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName


data class BusTampBookingResp (
  @SerializedName("isSuccess"   ) val isSuccess: Boolean,
  @SerializedName("returnMessage"   ) val returnMessage: String,
  @SerializedName("returnCode"   ) val returnCode: String,
  @SerializedName("data"   ) val data: Any?
)