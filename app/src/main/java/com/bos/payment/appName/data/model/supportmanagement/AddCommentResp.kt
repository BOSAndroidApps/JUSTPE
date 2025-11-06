package com.bos.payment.appName.data.model.supportmanagement

import com.google.gson.annotations.SerializedName

data class AddCommentResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: AddCommentData? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class AddCommentData(

	@field:SerializedName("complaintID")
	val complaintID: Int? = null,

	@field:SerializedName("commentDate")
	val commentDate: String? = null,

	@field:SerializedName("commentID")
	val commentID: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null
)
