package com.bos.payment.appName.data.model.notification

import com.google.gson.annotations.SerializedName


data class GetNotificationRes (

  @SerializedName("RID"                ) var RID                : Int?    = null,
  @SerializedName("NotificationID"     ) var NotificationID     : String? = null,
  @SerializedName("NotificationDate"   ) var NotificationDate   : String? = null,
  @SerializedName("agentType"          ) var agentType          : String? = null,
  @SerializedName("NotificationPicUrl" ) var NotificationPicUrl : String? = null,
  @SerializedName("Status"             ) var Status             : Boolean? = null,
  @SerializedName("message"            ) var message            : String? = null,
  @SerializedName("Value"              ) var Value              : String? = null

)