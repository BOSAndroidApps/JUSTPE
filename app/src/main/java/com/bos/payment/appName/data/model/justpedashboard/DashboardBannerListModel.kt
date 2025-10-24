package com.bos.payment.appName.data.model.justpedashboard

import com.google.gson.annotations.SerializedName

data class DashboardBannerListModel(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class DataItem(

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("imagePath")
	val imagePath: String? = null,

	@field:SerializedName("marchentCode")
	val marchentCode: String? = null,

	@field:SerializedName("modifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("modifiedBy")
	val modifiedBy: Any? = null,

	@field:SerializedName("urlRedirect")
	val urlRedirect: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null
)
