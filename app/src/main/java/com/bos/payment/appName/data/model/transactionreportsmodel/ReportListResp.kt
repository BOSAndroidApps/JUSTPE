package com.bos.payment.appName.data.model.transactionreportsmodel

import com.google.gson.annotations.SerializedName

data class ReportListResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: List<ReportDataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class ReportDataItem(

	@field:SerializedName("displayValue")
	val displayValue: String? = null,

	@field:SerializedName("displayText")
	val displayText: String? = null
)
