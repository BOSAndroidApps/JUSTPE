package com.bos.payment.appName.data.model.transactionreportsmodel

import com.google.gson.annotations.SerializedName

data class TransactionReportsResp(

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

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("upiRefID")
	val upiRefID: String? = null,

	@field:SerializedName("payoutExternalID")
	val payoutExternalID: Any? = null,

	@field:SerializedName("transactionno")
	val transactionno: String? = null,

	@field:SerializedName("servicE_TYPE")
	val servicETYPE: String? = null,

	@field:SerializedName("ipAddress")
	val ipAddress: Any? = null,

	@field:SerializedName("gst")
	val gst: String? = null,

	@field:SerializedName("transferto")
	val transferto: String? = null,

	@field:SerializedName("tdsAmt")
	val tdsAmt: Any? = null,

	@field:SerializedName("dr")
	val dr: String? = null,

	@field:SerializedName("cr")
	val cr: String? = null,

	@field:SerializedName("flagRemarks")
	val flagRemarks: Any? = null,

	@field:SerializedName("transferfrom")
	val transferfrom: String? = null,

	@field:SerializedName("sNo")
	val sNo: String? = null,

	@field:SerializedName("serviceCh")
	val serviceCh: Any? = null,

	@field:SerializedName("tranAmt")
	val tranAmt: Any? = null,

	@field:SerializedName("serviceCh_Gst")
	val serviceChGst: Any? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("retailerComm")
	val retailerComm: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
