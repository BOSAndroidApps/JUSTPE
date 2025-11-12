package com.bos.payment.appName.data.model.makepaymentnew

import com.google.gson.annotations.SerializedName

data class BankDetailsReq(

	@field:SerializedName("parmFlag")
	val parmFlag: String? = null,

	@field:SerializedName("accountNo")
	val accountNo: String? = null,

	@field:SerializedName("accountHolder_Name")
	val accountHolderName: String? = null,

	@field:SerializedName("adminCode")
	val adminCode: String? = null
)
