package com.bos.payment.appName.data.model.travel.bus.busTicket

import com.google.gson.annotations.SerializedName

data class BusTicketCancellationChargeRes(

	@field:SerializedName("serviceCharge")
	val serviceCharge: Any? = null,

	@field:SerializedName("cancellationCharge_Key")
	val cancellationChargeKey: String? = null,

	@field:SerializedName("response_Header")
	val responseHeader: ResponseHeaderr? = null,

	@field:SerializedName("cancellationPenaltyValues")
	val cancellationPenaltyValues: List<Any?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("cancellable")
	val cancellable: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class ResponseHeaderr(

	@field:SerializedName("status_Id")
	val statusId: String? = null,

	@field:SerializedName("error_Code")
	val errorCode: String? = null,

	@field:SerializedName("error_Desc")
	val errorDesc: String? = null,

	@field:SerializedName("error_InnerException")
	val errorInnerException: String? = null,

	@field:SerializedName("request_Id")
	val requestId: String? = null
)
