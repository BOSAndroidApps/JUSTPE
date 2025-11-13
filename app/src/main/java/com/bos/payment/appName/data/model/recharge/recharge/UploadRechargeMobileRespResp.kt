package com.bos.payment.appName.data.model.recharge.recharge

import com.google.gson.annotations.SerializedName

data class UploadRechargeMobileRespResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)
