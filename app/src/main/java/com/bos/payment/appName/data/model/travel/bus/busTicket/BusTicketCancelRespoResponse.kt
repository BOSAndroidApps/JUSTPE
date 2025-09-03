package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName

data class BusTicketCancelRespoResponse(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)
