package com.bos.payment.appName.data.model.makepaymentnew

import com.google.gson.annotations.SerializedName

data class ReferenceIDGenerateResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: MakePaymentData? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class MakePaymentData(

	@field:SerializedName("seriesCode")
	val seriesCode: String? = null
)
