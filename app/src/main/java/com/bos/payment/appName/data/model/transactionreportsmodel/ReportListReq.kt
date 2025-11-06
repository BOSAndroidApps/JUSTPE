package com.bos.payment.appName.data.model.transactionreportsmodel

import com.google.gson.annotations.SerializedName

data class ReportListReq(

	@field:SerializedName("p_parmFlag")
	val pParmFlag: String? = null,

	@field:SerializedName("p_parmFlag2")
	val pParmFlag2: String? = null,

	@field:SerializedName("p_parmFlag1")
	val pParmFlag1: String? = null
)
