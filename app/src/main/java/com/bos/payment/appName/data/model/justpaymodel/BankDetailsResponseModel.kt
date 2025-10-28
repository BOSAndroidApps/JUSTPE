package com.bos.payment.appName.data.model.justpaymodel

import com.google.gson.annotations.SerializedName

data class BankDetailsResponseModel(

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

	@field:SerializedName("settlementAccountIfsc")
	val settlementAccountIfsc: String? = null,

	@field:SerializedName("mobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("is_QRCodeGenerated")
	val isQRCodeGenerated: String? = null,

	@field:SerializedName("accountType")
	val accountType: String? = null,

	@field:SerializedName("businessName")
	val businessName: String? = null,

	@field:SerializedName("emailId")
	val emailId: String? = null,

	@field:SerializedName("settlementAccountNumber")
	val settlementAccountNumber: String? = null,

	@field:SerializedName("is_QRCodeActivate")
	val isQRCodeActivate: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("settlementAccountName")
	val settlementAccountName: String? = null,

	@field:SerializedName("staticQR")
	val staticQR: String? = null,


	@field:SerializedName("vpaid")
	val vpaid: String? = null,

	@field:SerializedName("sellerIdentifier")
	val sellerIdentifier: String? = null
)
