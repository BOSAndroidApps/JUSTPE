package com.bos.payment.appName.data.model.travel.bus.forservicecharge

import com.google.gson.annotations.SerializedName

data class BusCommissionResp(

	@field:SerializedName("returnCode")
	val returnCode: Any? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class DataItem(

	@field:SerializedName("productSource")
	val productSource: String? = null,

	@field:SerializedName("seatType")
	val seatType: String? = null,

	@field:SerializedName("retailerType")
	val retailerType: String? = null,

	@field:SerializedName("servicesValue")
	val servicesValue: Any? = null,

	@field:SerializedName("busCategory")
	val busCategory: String? = null,

	@field:SerializedName("servicesType")
	val servicesType: String? = null,

	@field:SerializedName("adminCode")
	val adminCode: String? = null,

	@field:SerializedName("userType")
	val userType: String? = null,

	@field:SerializedName("retailerID")
	val retailerID: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("commissionType")
	val commissionType: String? = null,

	@field:SerializedName("commissionValue")
	val commissionValue: Any? = null
)
