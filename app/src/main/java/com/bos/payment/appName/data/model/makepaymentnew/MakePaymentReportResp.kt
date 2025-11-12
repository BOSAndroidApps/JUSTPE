package com.bos.payment.appName.data.model.makepaymentnew

import com.google.gson.annotations.SerializedName

data class MakePaymentReportResp(
	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null,


	@field:SerializedName("data")
	val data: MutableList<DataItem?>? = null
)

data class DataItem(

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("refrenceID")
	val refrenceID: String? = null,

	@field:SerializedName("transactionID")
	val transactionID: String? = null,

	@field:SerializedName("documentPath")
	val documentPath: String? = null,

	@field:SerializedName("depositBankName")
	val depositBankName: String? = null,

	@field:SerializedName("approvedDateTime")
	val approvedDateTime: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("apporvedStatus")
	val apporvedStatus: String? = null,

	@field:SerializedName("registrationId")
	val registrationId: String? = null,

	@field:SerializedName("adminCode")
	val adminCode: String? = null,

	@field:SerializedName("recordDateTime")
	val recordDateTime: String? = null,

	@field:SerializedName("paymentDate")
	val paymentDate: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("approvedBy")
	val approvedBy: String? = null,

	@field:SerializedName("apporveRemakrs")
	val apporveRemakrs: String? = null,

	@field:SerializedName("branchCode_ChecqueNo")
	val branchCodeChecqueNo: String? = null
)
