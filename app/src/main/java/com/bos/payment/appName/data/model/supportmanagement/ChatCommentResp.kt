package com.bos.payment.appName.data.model.supportmanagement

import com.google.gson.annotations.SerializedName

data class ChatCommentResp(

	@field:SerializedName("returnCode")
	val returnCode: String? = null,

	@field:SerializedName("data")
	val data: ChatData? = null,

	@field:SerializedName("returnMessage")
	val returnMessage: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class ChatData(

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null
)

data class CommentsItem(

	@field:SerializedName("commentBy")
	val commentBy: String? = null,

	@field:SerializedName("commentDate")
	val commentDate: String? = null,

	@field:SerializedName("commentID")
	val commentID: Int? = null,

	@field:SerializedName("commentTo")
	val commentTo: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null
)
