package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GetToselfPayoutCommercialReq(

	@field:SerializedName("txtslabamtfrom")
	val txtslabamtfrom: Int? = null,

	@field:SerializedName("txtslabamtto")
	val txtslabamtto: Int? = null,

	@field:SerializedName("merchant")
	val merchant: String? = null,

	@field:SerializedName("modeofPayment")
	val modeofPayment: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null

)
