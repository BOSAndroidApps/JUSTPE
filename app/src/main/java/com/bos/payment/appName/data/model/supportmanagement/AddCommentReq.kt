package com.bos.payment.appName.data.model.supportmanagement

import com.google.gson.annotations.SerializedName

data class AddCommentReq(

	@field:SerializedName("complaintID")
	val complaintID: Int? = null,

	@field:SerializedName("commentBy")
	val commentBy: String? = null,

	@field:SerializedName("commentTo")
	val commentTo: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null
)
