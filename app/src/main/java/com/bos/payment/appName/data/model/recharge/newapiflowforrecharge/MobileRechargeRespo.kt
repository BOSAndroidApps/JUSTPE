package com.bos.payment.appName.data.model.recharge.newapiflowforrecharge

import com.google.gson.annotations.SerializedName

data class MobileRechargeRespo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("Status")
	val statusBos: String? = null,

	@field:SerializedName("data")
	val data: RechargeData? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("error")
	val error: Any? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class RechargeData(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("txnId")
	val txnId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("ackno")
	val ackno: String? = null
)
