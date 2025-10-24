package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GenerateVirtualBankDetailsResponseModel(

	@field:SerializedName("responsecode")
	val responsecode: Int? = null,

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("details")
	val details: Any? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
