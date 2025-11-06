package com.bos.payment.appName.data.model.transactionreportsmodel

import com.google.gson.annotations.SerializedName

data class TransactionReportsReq(

	@field:SerializedName("parmFlag")
	val parmFlag: String? = null,

	@field:SerializedName("paramMerchantCode")
	val paramMerchantCode: String? = null,

	@field:SerializedName("parmToDate")
	val parmToDate: String? = null,

	@field:SerializedName("parmUser")
	val parmUser: String? = null,

	@field:SerializedName("parmFla2")
	val parmFla2: String? = null,

	@field:SerializedName("parmFromDate")
	val parmFromDate: String? = null
)
