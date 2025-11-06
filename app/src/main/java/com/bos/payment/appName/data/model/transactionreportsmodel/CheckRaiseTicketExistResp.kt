package com.bos.payment.appName.data.model.transactionreportsmodel

import com.google.gson.annotations.SerializedName

data class CheckRaiseTicketExistResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class Data(

	@field:SerializedName("isValid")
	val isValid: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
