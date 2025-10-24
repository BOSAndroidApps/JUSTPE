package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class UpdateBankDetailsReq(

	@field:SerializedName("is_QRCodeActivate")
	val isQRCodeActivate: String? = null,

	@field:SerializedName("is_QRCodeGenerated")
	val isQRCodeGenerated: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("staticQRCodeIntent_url")
	val staticQRCodeIntentUrl: String? = null
)
