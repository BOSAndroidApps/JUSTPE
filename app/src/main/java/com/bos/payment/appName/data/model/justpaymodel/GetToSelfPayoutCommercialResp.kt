package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class GetToSelfPayoutCommercialResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: List<ToSelfDataItem?>? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class ToSelfDataItem(

	@field:SerializedName("admin_CommissionType")
	val adminCommissionType: String? = null,

	@field:SerializedName("serviceType")
	val serviceType: String? = null,

	@field:SerializedName("serviceCharge")
	val serviceCharge: String? = null,

	@field:SerializedName("retailer_Commission")
	val retailerCommission: String? = null,

	@field:SerializedName("typeofCrad")
	val typeofCrad: String? = null,

	@field:SerializedName("admin_Commission")
	val adminCommission: String? = null,

	@field:SerializedName("modeOfPayment")
	val modeOfPayment: String? = null,

	@field:SerializedName("retailer_CommissionType")
	val retailerCommissionType: String? = null,

	@field:SerializedName("customerCommissionType")
	val customerCommissionType: String? = null,

	@field:SerializedName("customerCommission")
	val customerCommission: String? = null
)
