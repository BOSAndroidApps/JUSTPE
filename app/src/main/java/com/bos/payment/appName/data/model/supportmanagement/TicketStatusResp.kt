package com.bos.payment.appName.data.model.supportmanagement

import com.google.gson.annotations.SerializedName

data class TicketStatusResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class TicketsItem(

	@field:SerializedName("ticketNumber")
	val ticketNumber: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("empDetails")
	val empDetails: Any? = null,

	@field:SerializedName("serviceName")
	val serviceName: String? = null,

	@field:SerializedName("transactionID")
	val transactionID: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("complaintID")
	val complaintID: Int? = null,

	@field:SerializedName("imagePath1")
	val imagePath1: String? = null,

	@field:SerializedName("imagePath3")
	val imagePath3: String? = null,

	@field:SerializedName("retailerDetails")
	val retailerDetails: String? = null,

	@field:SerializedName("imagePath2")
	val imagePath2: String? = null,

	@field:SerializedName("transactionSummary")
	val transactionSummary: String? = null,

	@field:SerializedName("status")
	val status: String? = null
){
	fun getImageUrls(baseUrl: String): List<String> {
		val urls = mutableListOf<String>()
		if (!imagePath1.isNullOrEmpty()) urls.add("$baseUrl$imagePath1")
		if (!imagePath2.isNullOrEmpty()) urls.add("$baseUrl$imagePath2")
		if (!imagePath3.isNullOrEmpty()) urls.add("$baseUrl$imagePath3")
		return urls
	}
}

data class Data(

	@field:SerializedName("tickets")
	val tickets: MutableList<TicketsItem?>? = null
)
