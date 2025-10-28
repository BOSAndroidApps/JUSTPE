package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class RetailerContactListResponseModel(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: List<RetailerDataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class RetailerDataItem(

	@field:SerializedName("agentType")
	val agentType: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("userID")
	val userID: String? = null,

	@field:SerializedName("agencyName")
	val agencyName: String? = null
)
