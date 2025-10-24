package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GenerateQRCodeResponse(

	@field:SerializedName("responsecode")
	val responsecode: Int? = null,

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("txnRefranceNo")
	val txnRefranceNo: String? = null,

	@field:SerializedName("details")
	val details: Details? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Details(

	@field:SerializedName("qrCode")
	val qrCode: String? = null,

	@field:SerializedName("vpa")
	val vpa: String? = null,

	@field:SerializedName("subvpa")
	val subvpa: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
