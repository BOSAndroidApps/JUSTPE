package com.bos.payment.appName.data.model.makepaymentnew

import com.google.gson.annotations.SerializedName

data class BankDetailsResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: List<BankDataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class BankDataItem(

	@field:SerializedName("companyCode")
	val companyCode: String? = null,

	@field:SerializedName("bank_Name")
	val bankName: String? = null,

	@field:SerializedName("panNo")
	val panNo: String? = null,

	@field:SerializedName("accountNo")
	val accountNo: String? = null,

	@field:SerializedName("accountType")
	val accountType: String? = null,

	@field:SerializedName("accountHolder_Name")
	val accountHolderName: String? = null,

	@field:SerializedName("branchName")
	val branchName: String? = null,

	@field:SerializedName("adminCode")
	val adminCode: String? = null,

	@field:SerializedName("ifsC_Code")
	val ifsCCode: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("status")
	val status: String? = null ,

	@field:SerializedName("staticQRCodeIntent_url")
	val staticQRCodeIntenturl: String? = null
)
